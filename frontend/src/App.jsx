import {useEffect, useState} from "react";

function App() {
    const [treningy, setTreningy] = useState([]);

    const [novyNazov, setNovyNazov] = useState("");
    const [noveSerie, setNoveSerie] = useState(4);
    const [noveOpakovania, setNoveOpakovania] = useState(10);

    const API_URL = "http://localhost:8080/api/treningy";

    useEffect(() => {
        fetch(API_URL)
            .then((response) => response.json())
            .then((vsetkyTreningy) => {
                const vsetkyCviky = vsetkyTreningy.flatMap(t => t.zaznamy || []);
                setTreningy(vsetkyCviky);
            })
            .catch((error) => console.error("Chyba pri načítaní dát : ", error))
    }, []);

    const prepniStav = (idCviku) => {
        const upraveneTreningy = treningy.map((t) => {
            if (t.id === idCviku) {
                return {...t, hotovo: !t.hotovo};
            }

            return t;
        });

        setTreningy((upraveneTreningy));
    };

    const pridajCvik = () => {
        if (novyNazov.trim() === "") return;

        const vygenerovaneSerie = Array.from({ length: noveSerie }, () => ({
            vaha: 60,
            pocetOpakovani: noveOpakovania
        }));

        const novyTrening = {
            datum: new Date().toISOString().split('T')[0],
            zaznamy: [
                {
                    cvik: {
                        nazovCviku: novyNazov,
                        svalovaPartia: "NOHY"
                    },
                    serie: vygenerovaneSerie
                }
            ]
        };

        fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(novyTrening),
        })
            .then((response) => response.json())
            .then((ulozenyTreningZoSpringu) => {
                if (ulozenyTreningZoSpringu.zaznamy && ulozenyTreningZoSpringu.zaznamy.length > 0) {
                    const novyZaznam = ulozenyTreningZoSpringu.zaznamy[0];
                    setTreningy([...treningy, novyZaznam]);
                }
                setNovyNazov("");
                setNoveSerie(4);
                setNoveOpakovania(10);


            })
            .catch((error) => console.error("Chyba pri ukladaní : ", error));
    }

    const vymazCvik = (idCviku) => {
        const upraveneTreningy = treningy.filter((t) => t.id !== idCviku);

        setTreningy(upraveneTreningy);
    }

    return (
        <div style={{fontFamily: 'Arial, sans-serif', padding: '20px'}}>
            <h1>🏋️‍♂️ Môj Tréningový Panel</h1>
            <p>Dáta pripravné na prepojenie s Javou</p>

            <table border="1" cellPadding="10"
                   style={{borderCollapse: 'collapse', width: '100%', maxWidth: '600px'}}>
                <thead>
                <tr style={{backgroundColor: '#f2f2f2'}}>
                    <th>Cvik</th>
                    <th>Séria</th>
                    <th>Opakovania</th>
                    <th>Stav</th>
                </tr>
                </thead>

                <tbody>
                {treningy.map((t) => (
                    <tr key={t.id}>
                        <td>{t.cvik ? t.cvik.nazovCviku : "Neznámy cvik"}</td>
                        <td>{t.serie ? t.serie.length : 0}</td>
                        <td>{t.serie && t.serie.length > 0 ? t.serie[0].pocetOpakovani : 0}</td>
                        <td>
                            <span>
                                {t.hotovo ? "✅ Splnené" : "❌ Čaká ma"}
                            </span>

                            <button
                                onClick={() => prepniStav(t.id)}
                                style={{
                                    marginRight: '5px',
                                    cursor: 'pointer',
                                    backgroundColor: '#008000',
                                    border: '1px solid #ccc',
                                    padding: '5px 10px',
                                    borderRadius: '4px'
                                }}
                            >
                                Zmeň
                            </button>

                            <button
                                onClick={() => vymazCvik(t.id)}
                                style={{
                                    cursor: 'pointer',
                                    backgroundColor: '#ff4d4d',
                                    color: 'white',
                                    border: 'none',
                                    padding: '5px 10px',
                                    borderRadius: '4px'
                                }}
                            >
                                Zmaž
                            </button>

                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div style={{
                backgroundColor: '#f9f9f9',
                padding: '15px',
                borderRadius: '8px',
                maxWidth: '600px',
                border: '1px solid #eee'
            }}>
                <h3 style={{marginTop: 0}}>➕ Pridať nový cvik</h3>
                <div style={{display: 'flex', gap: '10px', flexWrap: 'wrap', alignItems: 'center'}}>
                    <input
                        type="text"
                        placeholder="Názov cviku"
                        value={novyNazov}
                        onChange={(e) => setNovyNazov(e.target.value)}
                        style={{
                            padding: '8px',
                            width: '250px',
                            marginRight: '10px',
                            borderRadius: '4px',
                            border: '1px solid #ccc'
                        }}
                    />

                    <label>Série : </label>
                    <input
                        type="number"
                        min="1"
                        value={noveSerie}
                        onChange={(e) => setNoveSerie(Number(e.target.value))}
                        style={{padding: '8px', width: '50px', borderRadius: '4px', border: '1px solid #ccc'}}
                    />

                    <label>Opakovania : </label>
                    <input
                        type="number"
                        min="1"
                        value={noveOpakovania}
                        onChange={(e) => setNoveOpakovania(Number(e.target.value))}
                        style={{padding: '8px', width: '50px', borderRadius: '4px', border: '1px solid #ccc'}}
                    />

                    <button
                        onClick={pridajCvik}
                        style={{
                            cursor: 'pointer',
                            backgroundColor: '#4CAF50',
                            color: 'white',
                            border: 'none',
                            padding: '8px 15px',
                            borderRadius: '4px',
                            fontWeight: 'bold'
                        }}
                    >
                        Pridať cvik
                    </button>
                </div>
            </div>
        </div>
    );
}

export default App;