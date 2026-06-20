import {useEffect, useState} from "react";
import "./App.css";

function App() {
    const [treningy, setTreningy] = useState([]);

    const [novyNazov, setNovyNazov] = useState("");
    const [formularSerie, setFormularSerie] = useState([
        {id: 1, pocetOpakovani: 0, vaha: 0}
    ]);

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

    const ľprepniStav = (idCviku) => {
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

        const vygenerovaneSerie = formularSerie.map(s => ({
            vaha: s.vaha,
            pocetOpakovani: s.pocetOpakovani
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
                setFormularSerie([
                    { id: 1, pocetOpakovani: 10, vaha: 0}
                ]);
            })
            .catch((error) => console.error("Chyba pri ukladaní : ", error));
    }

    const vymazCvik = (idCviku) => {
        const upraveneTreningy = treningy.filter((t) => t.id !== idCviku);

        setTreningy(upraveneTreningy);
    }

    const pridajSeriuDoFormulara = () => {
        setFormularSerie([
            ...formularSerie, {id: Date.now(), pocetOpakovani: 10, vaha: 0}
        ]);
    };

    const odstranSeriuZFormulara = (id) => {
        if (formularSerie.length > 1) {
            setFormularSerie(formularSerie.filter(s => s.id !== id));
        }
    };

    const zmenSeriuVFormulari = (id, pole, hodnota) => {
        setFormularSerie(formularSerie.map(s =>
            s.id === id ? {...s, [pole]: Number(hodnota)} : s
        ));
    }

    return (
        <div className="app-container">
            <h1>🏋️‍♂️ Fitness tracker 🏋️‍♂️ </h1>
            <p className="subtitle">Gym session tracker</p>

            <table>
                <thead>
                <tr>
                    <th>Cvik</th>
                    <th>Séria</th>
                    <th>Opakovania</th>
                    <th>Stav</th>
                    <th>Akcie</th>
                </tr>
                </thead>

                <tbody>
                {treningy.map((t) => (
                    <tr key={t.id}>
                        <td>{t.cvik ? t.cvik.nazovCviku : "Neznámy cvik"}</td>
                        <td>{t.serie ? t.serie.length : 0}</td>
                        <td>{t.serie && t.serie.length > 0 ? t.serie[0].pocetOpakovani : 0}</td>
                        <td>
                            <span className={t.hotovo ? "status-done" : "status-todo"}>
                                    {t.hotovo ? "✅ Splnené" : "❌ Čaká ma"}
                            </span>
                        </td>

                        <td>
                            <div className="action-buttons">
                                <button className="btn-change" onClick={() => prepniStav(t.id)}>
                                    Zmeň
                                </button>

                                <button className="btn-delete" onClick={() => vymazCvik(t.id)}>
                                    Zmaž
                                </button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div className="add-exercise-box">
                <h3>➕ Pridať nový cvik</h3>

                <div className="form-grid">
                    <div className="form-group row-span">
                        <input
                            type="text"
                            placeholder="Názov cviku"
                            value={novyNazov}
                            onChange={(e) => setNovyNazov(e.target.value)}
                        />
                    </div>

                    <div className="form-series-list">
                        <h4>Série pre tento cvik : </h4>

                        {formularSerie.map((seria, index) => (
                            <div key={seria.id} className="form-series-row">
                                <span>{index + 1}. séria : </span>

                                <input
                                    type="number"
                                    min="1"
                                    placeholder="Opakovania"
                                    value={seria.pocetOpakovani}
                                    onChange={(e) => zmenSeriuVFormulari(seria.id, 'pocetOpakovani', e.target.value)}
                                />

                                <input
                                    type="number"
                                    min="0"
                                    placeholder="Váha (kg)"
                                    value={seria.vaha}
                                    onChange={(e) => zmenSeriuVFormulari(seria.id, 'vaha', e.target.value)}
                                />

                                {formularSerie.length > 1 && (
                                    <button
                                        type="button"
                                        className="btn-delete-row"
                                        onClick={() => odstranSeriuZFormulara(seria.id)}
                                    >
                                        ❌
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>

                    <div className="form-actions">
                        <button
                            type="button"
                            className="btn-add-series"
                            onClick={pridajSeriuDoFormulara}
                        >
                            ➕ Pridať sériu
                        </button>

                        <button className="btn-submit" onClick={pridajCvik}>
                            Pridať cvik
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}


            export default App;