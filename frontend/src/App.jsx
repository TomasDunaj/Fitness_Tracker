import React, {useEffect, useState} from "react";
import "./App.css";
import {LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie} from 'recharts';
import logo from "./assets/Fitness_tracker_LOGO.png";

const PREDDEFINOVANE_CVIKY = {
    HRUDNIK: ["Benchpress", "Tlaky s jednoručkami na šikmej lavičke", "Rozpažovanie", "Kľuky na bradlách (Dips)"],
    CHRBAT: ["Mŕtvy ťah", "Zťahovanie kladky na široko", "Príťahy jednoručky", "Zhyby"],
    NOHY: ["Drepy", "Legpress", "Výpady", "Predkopávanie", "Zakopávanie"],
    RAMENA: ["Tlaky nad hlavu s jednoručkami", "Military Press", "Upažovanie s jednoručkami"],
    RUKY: ["Bicepsový zdvih s činkou", "Tricepsové sťahovanie kladky", "Kladivové zdvihy"]
};

const NAZVY_PARTII = {
    HRUDNIK: "Hrudník",
    CHRBAT: "Chrbát",
    NOHY: "Nohy",
    RAMENA: "Ramená",
    RUKY: "Ruky"
};


function App() {
    const [treningy, setTreningy] = useState([]);

    const [activeTab, setActiveTab] = useState('trening');

    const [history, setHistory] = useState([]);

    const PRAZDNA_SERIA = {pocetOpakovani: "", vaha: ""}

    const [isModalOpen, setIsModalOpen] = useState(false);

    const [vybranaPartia, setVybranaPartia] = useState("HRUDNIK");

    const [vybranyCvik, setVybranyCvik] = useState(PREDDEFINOVANE_CVIKY.HRUDNIK[0]);

    const [vlastnyCvik, setVlastnyCvik] = useState("");

    const [statistikyPartii, setStatistikyPartii] = useState([]);

    const [databazoveCviky, setDatabazoveCviky] = useState([]);
    const [vybranyCvikProgresId, setVybranyCvikProgresId] = useState('');
    const [progresData, setProgresData] = useState([]);

    const [osobneRekordy, setOsobneRekordy] = useState([]);

    const [formularSerie, setFormularSerie] = useState([
        {id: 1, ...PRAZDNA_SERIA}
    ]);

    const [vybranyCvikId, setVybranyCvikId] = useState(null);

    const klikolNaCvik = (id) => {
        setVybranyCvikId(vybranyCvikId === id ? null : id);
    };

    const API_URL = "http://localhost:8080/api/treningy";

    useEffect(() => {
        fetch(API_URL)
            .then((response) => response.json())
            .then((vsetkyTreningy) => {
                const tzoffset = (new Date()).getTimezoneOffset() * 60000;
                const dnesnyDatum = (new Date(Date.now() - tzoffset)).toISOString().split('T')[0];
                const currentTraining = vsetkyTreningy.filter(t => t.datum === dnesnyDatum);
                const currentExercises = currentTraining.flatMap(t => t.zaznamy || []);

                setTreningy(currentExercises);
            })


            .catch((error) => console.error("Chyba pri načítaní dát : ", error))
    }, []);

    useEffect(() => {
        if (activeTab === 'historia') {
            fetch('http://localhost:8080/api/zaznamy')
                .then(response => response.json())
                .then(data => {
                    console.log("DÁTA : ", data);
                    setHistory(data);
                })
                .catch(error => console.error("Chyba pri načítaní histórie : ", error));
        }

        if (activeTab === 'statistiky') {
            fetch('http://localhost:8080/api/treningy/statistiky/partie')
                .then(response => response.json())
                .then(data => {
                    console.log("Statistiky z backendu : ", data);
                    setStatistikyPartii(data);
                })
                .catch(error => console.error("Chyba pri načítaní štatistík : ", error));

        }
    }, [activeTab]);

    useEffect(() => {
        if (activeTab === 'statistiky') {
            fetch('http://localhost:8080/api/cviky')
                .then(response => response.json())
                .then(data => {
                    setDatabazoveCviky(data);
                    if (data.length > 0) {
                        setVybranyCvikProgresId(data[0].id)
                    }
                })
                .catch(error => console.error("Chyba pri načítaní cvikov pre graf : ", error));
        }
    }, [activeTab]);

    useEffect(() => {
        if (activeTab === 'statistiky' && vybranyCvikProgresId) {
            fetch(`http://localhost:8080/api/treningy/statistiky/progres?cvikId=${vybranyCvikProgresId}`)
                .then(response => response.json())
                .then(data => {
                    const formatovaneData = data.map(item => ({
                        ...item,
                        zobrazovanyDatum: new Date(item.datum).toLocaleDateString('sk-SK', {
                            day: '2-digit',
                            month: '2-digit'
                        })
                    }));
                    setProgresData(formatovaneData);
                })
                .catch(error => console.error("Chyba pri načítaní progresu cviku: ", error));
        }
    }, [vybranyCvikProgresId, activeTab]);


    const prepniStav = (idZaznamu) => {
        const povodnyStav = [...treningy];

        const upraveneTreningy = treningy.map((t) => {
            if (t.id === idZaznamu) {
                return {...t, splnene: !t.splnene};
            }

            return t;
        });
        setTreningy((upraveneTreningy));

        fetch(`http://localhost:8080/api/zaznamy/${idZaznamu}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to save");
                }
                return response.json();
            })
            .catch((error) => {
                console.error("Chyba pri prepínaní stavu na backende : ", error);
                setTreningy(povodnyStav);
                alert("Nepodarilo sa uložiť stav tréningu na server.");
            });
    };

    const pridajCvik = () => {
        const finalnyNazov = vybranyCvik === "VLASTNY" ? vlastnyCvik.trim() : vybranyCvik;

        if (!finalnyNazov || finalnyNazov === "") {
            alert("Zadaj alebo vyber názov cviku!");
            return;
        }

        const vygenerovaneSerie = formularSerie.map(s => ({
            vaha: s.vaha,
            pocetOpakovani: s.pocetOpakovani
        }));

        const tzoffset = (new Date()).getTimezoneOffset() * 60000;
        const lokalnyDnesnyDatum = (new Date(Date.now() - tzoffset)).toISOString().split('T')[0];

        const novyTrening = {
            datum: lokalnyDnesnyDatum,
            zaznamy: [
                {
                    cvik: {
                        nazovCviku: finalnyNazov,
                        svalovaPartia: vybranaPartia
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
                setVlastnyCvik("");
                setVybranyCvik(PREDDEFINOVANE_CVIKY[vybranaPartia][0]);
                setFormularSerie([
                    {id: 1, ...PRAZDNA_SERIA}
                ]);
            })
            .catch((error) => console.error("Chyba pri ukladaní : ", error));
    }

    const vymazCvik = (idCviku) => {


        fetch(`http://localhost:8080/api/zaznamy/${idCviku}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    const upraveneTreningy = treningy.filter((t) => t.id !== idCviku);
                    setTreningy(upraveneTreningy);
                } else {
                    alert("Backend vrátil chybu, cvik sa nepodarilo vymazať.");
                }
            })
            .catch((error) => {
                console.error("Chyba siete : ", error);
                alert("Nepodarilo sa spojiť so serverom.")
            });
    };

    const pridajSeriuDoFormulara = () => {
        setFormularSerie([
            ...formularSerie, {id: Date.now(), ...PRAZDNA_SERIA}
        ]);
    };

    const odstranSeriuZFormulara = (id) => {
        if (formularSerie.length > 1) {
            setFormularSerie(formularSerie.filter(s => s.id !== id));
        }
    };

    const zmenSeriuVFormulari = (id, pole, hodnota) => {
        if (hodnota === "") {
            setFormularSerie(formularSerie.map(s =>
                s.id === id ? {...s, [pole]: ""} : s
            ));
            return;
        }

        const cislo = Number(hodnota);

        const minHodnota = pole === 'pocetOpakovani' ? 1 : 0;
        const upraveneCislo = cislo < minHodnota ? minHodnota : cislo;

        setFormularSerie(formularSerie.map(s =>
            s.id === id ? {...s, [pole]: upraveneCislo} : s
        ));
    }

    const nacitajOsobneRekordy = async () => {
        setIsModalOpen(true);
        try {
            const response = await fetch('http://localhost:8080/api/zaznamy/osobne-rekordy');
            const data = await response.json();
            setOsobneRekordy(data);
        } catch (error) {
            console.error("Chyba pri načítaní osobných rekordov : ", error)
        }
    };

    return (
        <div className="app-container">

            <aside className="sidebar">
                <div className="sidebar-logo">
                    <img src={logo} className="app-logo" alt="Fitness Tracker Logo"/>
                </div>

                <nav className="sidebar-menu">
                    <button className={`menu-item ${activeTab === 'trening' ? 'active' : ''}`}
                            onClick={() => setActiveTab('trening')}
                    >
                        🏋️‍♂️ Dnešný tréning
                    </button>
                    <button className={`menu-item ${activeTab === 'historia' ? 'active' : ''}`}
                            onClick={() => setActiveTab('historia')}
                    >
                        📅 História
                    </button>
                    <button className={`menu-item ${activeTab === 'statistiky' ? 'active' : ''}`}
                            onClick={() => setActiveTab('statistiky')}
                    >
                        📊 Štatistiky
                    </button>
                </nav>
            </aside>

            <main className="main-content">
                {activeTab === 'trening' && (
                    <>
                        <h1> Fitness tracker </h1>
                        <p className="subtitle">Track your gains, crush your goals</p>
                        <div className="dashboard-grid">
                            <table>
                                <thead>
                                <tr>
                                    <th>Cvik</th>
                                    <th>Série</th>
                                    <th>Stav</th>
                                    <th>Akcie</th>
                                </tr>
                                </thead>

                                <tbody>
                                {treningy.map((t) => (
                                    <React.Fragment key={t.id}>
                                        <tr onClick={() => klikolNaCvik(t.id)}>
                                            <td>{t.cvik ? t.cvik.nazovCviku : "Neznámy cvik"}</td>
                                            <td>{t.serie ? t.serie.length : 0}</td>
                                            <td>
                                <span className={t.splnene ? "status-done" : "status-todo"}>
                                    {t.splnene ? "Splnené" : "Čaká ma"}
                                </span>
                                            </td>
                                            <td>
                                                <div className="action-buttons">
                                                    <button className="btn-change" onClick={(e) => {
                                                        e.stopPropagation();
                                                        prepniStav(t.id);
                                                    }}>
                                                        Zmeň
                                                    </button>

                                                    <button className="btn-delete" onClick={(e) => {
                                                        e.stopPropagation();
                                                        vymazCvik(t.id)
                                                    }}>
                                                        Zmaž
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>


                                        {vybranyCvikId === t.id && (
                                            <tr>
                                                <td colSpan={5}>
                                                    <ul>
                                                        {t.serie && t.serie.map((seria, index) => (
                                                            <li key={seria.id || index}>
                                                                {index + 1}. séria : {seria.pocetOpakovani}x
                                                                @ {seria.vaha}kg
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </td>
                                            </tr>
                                        )}
                                    </React.Fragment>
                                ))}
                                </tbody>
                            </table>


                            <div className="add-exercise-box">
                                <h3>➕ Pridať nový cvik</h3>

                                <div className="form-grid">
                                    <div className="form-group row-span">
                                        <label className="form-label label-spacing"> Svalová partia : </label>
                                        <select
                                            value={vybranaPartia}
                                            onChange={(e) => {
                                                const novaPartia = e.target.value;
                                                setVybranaPartia(novaPartia)
                                                setVybranyCvik(PREDDEFINOVANE_CVIKY[novaPartia][0]);
                                            }}
                                            className="form-select"
                                        >
                                            {Object.keys(PREDDEFINOVANE_CVIKY).map((partia) => (
                                                <option key={partia} value={partia}>
                                                    {NAZVY_PARTII[partia]}
                                                </option>
                                            ))}
                                        </select>

                                        <label className="form-label label-spacing">Cvik : </label>
                                        <select
                                            value={vybranyCvik}
                                            onChange={(e) => setVybranyCvik(e.target.value)}
                                            className="form-select"
                                        >
                                            {PREDDEFINOVANE_CVIKY[vybranaPartia].map((cvik) => (
                                                <option key={cvik} value={cvik}>
                                                    {cvik}
                                                </option>
                                            ))}
                                            <option value="VLASTNY">-- Iný/Vlastný --</option>
                                        </select>

                                        {vybranyCvik === "VLASTNY" && (
                                            <input
                                                type="text"
                                                placeholder="Napíš názov vlastného cviku"
                                                value={vlastnyCvik}
                                                onChange={(e) => setVlastnyCvik(e.target.value)}
                                                className="form-input-vlastny"
                                            />
                                        )}
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
                    </>
                )}

                {activeTab === 'historia' && (
                    <div className="page-section history-container">
                        <h2>📅 História tréningov</h2>

                        {history.length === 0 ? (
                            <p>Zatiaľ nemáš uložené ziadne tréningy alebo sa načítavajú...</p>
                        ) : (
                            <div className="history-list">
                                {history.map((workout) => (
                                    <div key={workout.id} className="history-card">
                                        <div className="card-header">
                                            <h3>
                                                {workout.cvik?.nazovCviku || "Neznámy cvik"}
                                            </h3>
                                            <span className="workout-date">
                                                {workout.trening?.datum || `Záznam #${workout.id}`}
                                            </span>
                                        </div>
                                        <div className="card-body">
                                            <ul className="history-series-list">
                                                {workout.serie && workout.serie.map((seria, index) => (
                                                    <li key={seria.id || index}>
                                                        <strong>{index + 1}. séria:</strong> {seria.pocetOpakovani}x
                                                        @ {seria.vaha} kg
                                                    </li>
                                                ))}
                                            </ul>

                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                )}

                {activeTab === 'statistiky' && (
                    <div className="page-section statistiky-page">
                        <h2>📊 Štatistiky</h2>
                        <p>Grafy a progres tvojich výkonov.</p>

                        <div className="stats-top-row">
                            <div className="sidebar-stats">
                                <div className="stats-header-container">

                                    <h3>Tvoj progres</h3>

                                    {databazoveCviky.length > 0 && (
                                        <select
                                            value={vybranyCvikProgresId}
                                            onChange={(e) => setVybranyCvikProgresId(e.target.value)}
                                            className="stats-dropdown"
                                        >
                                            {databazoveCviky.map(cvik => (
                                                <option key={cvik.id} value={cvik.id}>
                                                    {cvik.nazovCviku} {`(${NAZVY_PARTII[cvik.svalovaPartia]})`}
                                                </option>
                                            ))}
                                        </select>
                                    )}
                                </div>

                                <div className="chart-wrapper">
                                    {progresData.length > 0 ? (
                                        <ResponsiveContainer>
                                            <LineChart data={progresData}
                                                       margin={{top: 10, right: 20, left: -20, bottom: 0}}>
                                                <XAxis dataKey="zobrazovanyDatum" stroke="#888888" fontSize={12}/>
                                                <YAxis stroke="#888888" fontSize={12} unit=" kg"/>
                                                <Tooltip
                                                    contentStyle={{
                                                        backgroundColor: '#222',
                                                        borderColor: '#444',
                                                        color: '#fff'
                                                    }}
                                                    formatter={value => [`${value} kg`, 'Max. váha']}
                                                />
                                                <Line
                                                    type="monotone"
                                                    dataKey="vaha"
                                                    stroke="#007bff"
                                                    strokeWidth={3}
                                                    activeDot={{r: 6}}
                                                />
                                            </LineChart>
                                        </ResponsiveContainer>
                                    ) : (
                                        <div className="no-data-message">
                                            Pre tento cvik zatiaľ nemáš zapísané žiadne tréningy.
                                        </div>
                                    )}
                                </div>
                            </div>
                        </div>

                        <div className="stats-bottom-row">

                            <div className="sidebar-stats pie-chart-box">
                                <h3>Zastúpenie partií</h3>
                                <div style={{
                                    width: '100%',
                                    height: 120,
                                    display: 'flex',
                                    justifyContent: 'center',
                                    alignItems: 'center'
                                }}>
                                    <ResponsiveContainer>
                                        <PieChart>
                                            <Pie
                                                data={statistikyPartii}
                                                cx="50%"
                                                cy="50%"
                                                innerRadius={35}
                                                outerRadius={50}
                                                paddingAngle={5}
                                                dataKey="value"
                                            />
                                            <Tooltip
                                                contentStyle={{
                                                    backgroundColor: '#222',
                                                    borderColor: '#444',
                                                    color: '#fff'
                                                }}
                                            />
                                        </PieChart>
                                    </ResponsiveContainer>
                                </div>
                            </div>

                            <div className="records-card-wrapper">
                                <div className="records-card" onClick={() => nacitajOsobneRekordy()}>
                                    <div className="records-card-icon">🏆</div>
                                    <div className="records-card-info">
                                        <h4>Osobné rekordy</h4>
                                        <p>Klikni pre zobrazenie PRs</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {isModalOpen && (
                            <div className="modal-overlay" onClick={() => setIsModalOpen(false)}>
                                <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                                    <button className="modal-close" onClick={() => setIsModalOpen(false)}>×</button>

                                    <h2>🏆 Tvoje osobné rekordy</h2>

                                    <div className="records-list">
                                        {osobneRekordy.map((rekord, index) => (
                                            <div className="record-item" key={rekord.nazovCviku || index}>
                                                <span>{index+1}. {rekord.nazovCviku} : </span>
                                                <strong>{rekord.vaha}</strong>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </main>
        </div>
    );
}

export default App;