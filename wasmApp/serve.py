import http.server
import socketserver
import os
import sys

PORT = 8080
DIRECTORY = os.path.join(os.path.dirname(__file__), "build", "dist", "wasmJs", "productionExecutable")

class WasmHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_OPTIONS(self):
        if self.path.startswith(("/api/f1", "/f1-live")):
            self.send_response(204)
            self.send_header("Access-Control-Allow-Origin", "*")
            self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
            self.send_header("Access-Control-Allow-Headers", "*")
            self.end_headers()
            return
        super().do_OPTIONS()

    def do_GET(self):
        if self.path.startswith(("/api/f1", "/f1-live")):
            import urllib.request
            subpath = self.path
            for prefix in ("/api/f1", "/f1-live"):
                if subpath.startswith(prefix):
                    subpath = subpath[len(prefix):]
                    break
            target_url = "https://livetiming.formula1.com" + subpath
            try:
                headers = {"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"}
                for h in ("Authorization", "Cookie"):
                    if h in self.headers:
                        headers[h] = self.headers[h]
                req = urllib.request.Request(target_url, headers=headers)
                with urllib.request.urlopen(req, timeout=5) as res:
                    self.send_response(res.status)
                    self.send_header("Access-Control-Allow-Origin", "*")
                    self.send_header("Content-Type", res.headers.get("Content-Type", "application/json"))
                    self.end_headers()
                    self.wfile.write(res.read())
            except urllib.error.HTTPError as e:
                self.send_response(e.code)
                self.send_header("Access-Control-Allow-Origin", "*")
                self.send_header("Content-Type", "application/json")
                self.end_headers()
                self.wfile.write(e.read())
            except Exception as e:
                self.send_response(502)
                self.send_header("Access-Control-Allow-Origin", "*")
                self.end_headers()
                self.wfile.write(str(e).encode("utf-8"))
            return
        super().do_GET()

    def end_headers(self):
        self.send_header("Cross-Origin-Opener-Policy", "same-origin")
        self.send_header("Cross-Origin-Embedder-Policy", "require-corp")
        
        path = self.path.split("?")[0]
        if path.endswith((".wasm", ".js", ".png", ".webp", ".svg", ".ttf", ".woff2")):
            self.send_header("Cache-Control", "public, max-age=86400")
        else:
            self.send_header("Cache-Control", "no-cache, must-revalidate")
            
        super().end_headers()

    def guess_type(self, path):
        if path.endswith(".wasm"):
            return "application/wasm"
        return super().guess_type(path)

if __name__ == "__main__":
    os.chdir(DIRECTORY)
    server = http.server.ThreadingHTTPServer(("", PORT), WasmHandler)
    server.daemon_threads = True
    print(f"Serving Wasm at http://localhost:{PORT}")
    sys.stdout.flush()
    server.serve_forever()
