const { onRequest } = require("firebase-functions/v2/https");

/**
 * High-performance CORS Proxy for F1 Live Timing
 * Forwards requests to https://livetiming.formula1.com with CORS headers attached.
 */
exports.f1Proxy = onRequest(
  {
    cors: true,
    region: "us-central1",
    maxInstances: 10,
    timeoutSeconds: 30,
    memory: "256MiB",
  },
  async (req, res) => {
    // Handle CORS pre-flight
    res.set("Access-Control-Allow-Origin", "*");
    res.set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    res.set("Access-Control-Allow-Headers", "*");
    res.set("Access-Control-Allow-Credentials", "true");

    if (req.method === "OPTIONS") {
      res.status(204).send("");
      return;
    }

    // Strip prefix: /api/f1/static/... -> /static/...
    const subPath = req.url.replace(/^\/api\/f1/, "").replace(/^\/f1-live/, "") || "/static/SessionInfo.json";
    const targetUrl = `https://livetiming.formula1.com${subPath}`;

    try {
      const headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36",
        "Accept": "*/*",
      };

      if (req.headers.authorization) {
        headers["Authorization"] = req.headers.authorization;
      }
      if (req.headers.cookie) {
        headers["Cookie"] = req.headers.cookie;
      }

      const response = await fetch(targetUrl, {
        method: req.method,
        headers,
        body: req.method !== "GET" && req.method !== "HEAD" ? req.body : undefined,
      });

      res.status(response.status);

      const contentType = response.headers.get("content-type");
      if (contentType) {
        res.set("Content-Type", contentType);
      }

      const data = await response.arrayBuffer();
      res.send(Buffer.from(data));
    } catch (err) {
      res.status(502).json({
        error: "Failed to connect to F1 Live Timing Gateway",
        message: err.message,
      });
    }
  }
);
