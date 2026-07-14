import React, {useEffect, useState} from "react";
import "./App.css";
import { LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer, PieChart, Pie} from 'recharts';
import logo from "./assets/Fitness_tracker_LOGO.png";

function App() {
    const [treningy, setTreningy] = useState([]);

    const [novyNazov, setNovyNazov] = useState("");

    const [activeTab, setActiveTab] = useState('trening');

    const [history, setHistory] = useState([]);

    const PRAZDNA_SERIA = {pocetOpakovani: "", vaha: ""}

    const [isModalOpen, setIsModalOpen] = useState(false);

    const testData = [
        {trening: '1.7.', vaha: 60},
        {trening: '4.7.', vaha: 62.5},
        {trening: '8.7.', vaha: 65},
        {trening: '11.7.', vaha: 65},
        {trening: '13.7.', vaha: 67.5},
    ];

    const svalovePartieData = [
        { name: 'Nohy', value: 4, fill: '#007bff' },
        { name: 'Hrudník', value: 3, fill: '#28a745' },
        { name: 'Chrbát', value: 5, fill: '#ffc107' },
        { name: 'Ramená', value: 2, fill: '#dc3545' },
        { name: 'Ruky', value: 3, fill: '#17a2b8' },
    ];

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
    }, [activeTab]);

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
        if (novyNazov.trim() === "") return;

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
                    {id: 1, ...PRAZDNA_SERIA}
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

    return (
        <div className="app-container">

            <aside className="sidebar">
                <div className="sidebar-logo">
                    <img src={logo} className="app-logo" alt="Fitness Tracker Logo" />
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

                        {/* 1. Horná polovica: Čiarový graf na celú šírku */}
                        <div className="stats-top-row">
                            <div className="sidebar-stats">
                                <h3>Tvoj progres</h3>
                                <div style={{width: '100%', height: 250}}>
                                    <ResponsiveContainer>
                                        <LineChart data={testData} margin={{top: 10, right: 20, left: -20, bottom: 0}}>
                                            <XAxis dataKey="trening" stroke="#888888" fontSize={12}/>
                                            <YAxis stroke="#888888" fontSize={12}/>
                                            <Tooltip
                                                contentStyle={{backgroundColor: '#222', borderColor: '#444', color: '#fff'}}
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
                                </div>
                            </div>
                        </div>

                        {/* 2. Spodná polovica: Rozdelená 50/50 na Pie Chart a Kartičku s PRs */}
                        <div className="stats-bottom-row">

                            {/* ĽAVÁ STRANA: Koláčový graf */}
                            <div className="sidebar-stats pie-chart-box">
                                <h3>Zastúpenie partií</h3>
                                <div style={{width: '100%', height: 120, display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                                    <ResponsiveContainer>
                                        <PieChart>
                                            <Pie
                                                data={svalovePartieData}
                                                cx="50%"
                                                cy="50%"
                                                innerRadius={35}
                                                outerRadius={50}
                                                paddingAngle={5}
                                                dataKey="value"
                                            />
                                            <Tooltip
                                                contentStyle={{backgroundColor: '#222', borderColor: '#444', color: '#fff'}}
                                            />
                                        </PieChart>
                                    </ResponsiveContainer>
                                </div>
                            </div>

                            <div className="records-card-wrapper">
                                <div className="records-card" onClick={() => setIsModalOpen(true)}>
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
                                        <div className="record-item">
                                            <span>Benchpress:</span>
                                            <strong>85 kg</strong>
                                        </div>
                                        <div className="record-item">
                                            <span>Drep:</span>
                                            <strong>110 kg</strong>
                                        </div>
                                        <div className="record-item">
                                            <span>Mŕtvy ťah:</span>
                                            <strong>140 kg</strong>
                                        </div>
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