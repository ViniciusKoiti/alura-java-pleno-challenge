package br.com.alura.AluraFake;

import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @GetMapping
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>AluraFake API</title>
                <style>
                    :root {
                        --bg: #0f172a;
                        --card: #111827;
                        --accent: #38bdf8;
                        --text: #e5e7eb;
                        --muted: #9ca3af;
                        --border: #1f2937;
                        --shadow: 0 10px 30px rgba(0,0,0,0.35);
                    }
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body {
                        font-family: "Segoe UI", system-ui, -apple-system, sans-serif;
                        background: radial-gradient(circle at 20% 20%, rgba(56,189,248,0.15), transparent 25%),
                                    radial-gradient(circle at 80% 0%, rgba(56,189,248,0.1), transparent 20%),
                                    var(--bg);
                        color: var(--text);
                        min-height: 100vh;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        padding: 32px;
                    }
                    .shell {
                        width: min(900px, 100%);
                        background: linear-gradient(180deg, rgba(255,255,255,0.02), rgba(255,255,255,0.01));
                        border: 1px solid var(--border);
                        border-radius: 16px;
                        box-shadow: var(--shadow);
                        padding: 28px;
                    }
                    header {
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        margin-bottom: 24px;
                    }
                    h1 { font-size: 24px; font-weight: 700; letter-spacing: 0.5px; }
                    .badge {
                        padding: 6px 12px;
                        border-radius: 12px;
                        background: rgba(56,189,248,0.12);
                        border: 1px solid rgba(56,189,248,0.35);
                        color: var(--accent);
                        font-size: 12px;
                        text-transform: uppercase;
                        letter-spacing: 0.8px;
                    }
                    p.lead { color: var(--muted); margin-bottom: 18px; line-height: 1.4; }
                    .grid {
                        display: grid;
                        gap: 16px;
                        grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
                    }
                    .card {
                        background: var(--card);
                        border: 1px solid var(--border);
                        border-radius: 12px;
                        padding: 16px;
                        transition: transform 0.15s ease, border-color 0.15s ease;
                    }
                    .card:hover {
                        transform: translateY(-3px);
                        border-color: rgba(56,189,248,0.5);
                    }
                    .card h2 { font-size: 18px; margin-bottom: 8px; }
                    .card p { color: var(--muted); font-size: 14px; margin-bottom: 12px; }
                    .card a {
                        color: var(--accent);
                        text-decoration: none;
                        font-weight: 600;
                    }
                    .card a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="shell">
                    <header>
                        <div>
                            <h1>AluraFake API</h1>
                            <p class="lead">Endpoints simples para usuários, cursos e publicação.</p>
                        </div>
                        <div class="badge">API Ready</div>
                    </header>
                    <div class="grid">
                        <div class="card">
                            <h2>Usuários</h2>
                            <p>Liste usuários cadastrados ou crie novos via POST.</p>
                            <a href="/user/all">GET /user/all</a>
                        </div>
                        <div class="card">
                            <h2>Cursos</h2>
                            <p>Veja cursos existentes e publique cursos quando prontos.</p>
                            <a href="/course/all">GET /course/all</a>
                        </div>
                        <div class="card">
                            <h2>Documentação rápida</h2>
                            <p>Envie payloads JSON para criar usuários, cursos e atividades.</p>
                            <a href="https://spring.io/guides" target="_blank" rel="noreferrer">Guia Spring</a>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """;
    }
}
