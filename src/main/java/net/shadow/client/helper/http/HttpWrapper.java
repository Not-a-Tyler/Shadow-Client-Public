package net.shadow.client.helper.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HttpWrapper {

    final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(HttpClient.Redirect.ALWAYS).connectTimeout(Duration.ofSeconds(20)).build();

    final ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public HttpWrapper() {

    }

    public HttpResponse<String> get(String uri, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().GET().setHeader("User-Agent", "MoleHttp/1.0");
        builder.uri(URI.create(uri));
        for (String header : headers) {
            String[] parsedheader = header.split(":");
            builder.setHeader(parsedheader[0], parsedheader[1]);
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void getAsync(String uri, String... headers) {
        pool.execute(() -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder().GET().setHeader("User-Agent", "MoleHttp/1.0");
            builder.uri(URI.create(uri));
            for (String header : headers) {
                String[] parsedheader = header.split(":");
                builder.setHeader(parsedheader[0], parsedheader[1]);
            }

            HttpRequest request = builder.build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public HttpResponse<String> post(String uri, String data, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
        builder.uri(URI.create(uri));
        for (String header : headers) {
            String[] parsedheader = header.split(":");
            builder.setHeader(parsedheader[0], parsedheader[1]);
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void postAsync(String uri, String data, String... headers) {
        pool.execute(() -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
            builder.uri(URI.create(uri));
            for (String header : headers) {
                String[] parsedheader = header.split(":");
                builder.setHeader(parsedheader[0], parsedheader[1]);
            }

            HttpRequest request = builder.build();

            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public HttpResponse<String> delete(String uri, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().DELETE().setHeader("User-Agent", "MoleHttp/1.0");
        builder.uri(URI.create(uri));
        for (String header : headers) {
            String[] parsedheader = header.split(":");
            builder.setHeader(parsedheader[0], parsedheader[1]);
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void deleteAsync(String uri, String... headers) {
        pool.execute(() -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder().DELETE().setHeader("User-Agent", "MoleHttp/1.0");
            builder.uri(URI.create(uri));
            for (String header : headers) {
                String[] parsedheader = header.split(":");
                builder.setHeader(parsedheader[0], parsedheader[1]);
            }

            HttpRequest request = builder.build();
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public HttpResponse<String> put(String uri, String data, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
        builder.uri(URI.create(uri));
        for (String header : headers) {
            String[] parsedheader = header.split(":");
            builder.setHeader(parsedheader[0], parsedheader[1]);
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void putAsync(String uri, String data, String... headers) {
        pool.execute(() -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
            builder.uri(URI.create(uri));
            for (String header : headers) {
                String[] parsedheader = header.split(":");
                builder.setHeader(parsedheader[0], parsedheader[1]);
            }

            HttpRequest request = builder.build();
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public HttpResponse<String> patch(String uri, String data, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder().method("PATCH", HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
        builder.uri(URI.create(uri));
        for (String header : headers) {
            String[] parsedheader = header.split(":");
            builder.setHeader(parsedheader[0], parsedheader[1]);
        }

        HttpRequest request = builder.build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void patchAsync(String uri, String data, String... headers) {
        pool.execute(() -> {
            HttpRequest.Builder builder = HttpRequest.newBuilder().method("PATCH", HttpRequest.BodyPublishers.ofString(data)).setHeader("User-Agent", "MoleHttp/1.0").setHeader("Content-Type", "application/json");
            builder.uri(URI.create(uri));
            for (String header : headers) {
                String[] parsedheader = header.split(":");
                builder.setHeader(parsedheader[0], parsedheader[1]);
            }

            HttpRequest request = builder.build();
            try {
                client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}
