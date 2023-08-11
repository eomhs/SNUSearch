package main;

import program.AdminProgram;
import program.SearchProgram;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class HttpServer {
    public static void main(String[] args) {
        int port = 8080; // If the 8080 port isn't available, try to use another port number.
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);
            AdminProgram ap = new AdminProgram();
            SearchProgram sp = new SearchProgram();
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Create a new thread to handle the client request
                Thread thread = new Thread(new ClientHandler(clientSocket, ap, sp));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private AdminProgram ap;
        private SearchProgram sp;

        public ClientHandler(Socket clientSocket, AdminProgram ap, SearchProgram sp)
        {
            this.clientSocket = clientSocket;
            this.ap = ap;
            this.sp = sp;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String request = in.readLine();
                System.out.println("Received request: " + request);

                // Extract the path from the request
                String[] requestParts = request.split(" ");
                String path = requestParts[1];
                System.out.println(path);

                // Store log in LOG_PATH
                ap.storeLog(path);

                // Read the HTML file and send it as the response
                if (path.equals("/")) {  // CASE 1: It sends index.html page to the client.
                    String filePath = "src/index.html";  // Default file to serve
                    File file = new File(filePath);  // Replace with the actual path to your HTML files
                    if (file.exists() && file.isFile()) {
                        System.out.println(file.getAbsolutePath());
                        String contentType = Files.probeContentType(Paths.get(filePath));
                        String content = new String(Files.readAllBytes(Paths.get(filePath)));

                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: " + contentType);
                        out.println("Content-Length: " + content.length());
                        out.println();
                        out.println(content);
                    } else {
                        // File not found
                        out.println("HTTP/1.1 404 Not Found");
                        out.println();
                    }
                }
                // User function lists
                else if (path.startsWith("/user")) {
                    // Find id and pw
                    int queryStartIndex = path.indexOf('?');
                    String queryParameters = path.substring(queryStartIndex + 1);
                    String[] keyAndValue = queryParameters.split("&");
                    String id, pw;
                    try {
                        id = keyAndValue[0].split("=")[1];
                    }
                    catch (Exception e){
                        id = "";
                    }
                    try{
                        pw = keyAndValue[1].split("=")[1];
                    }
                    catch (Exception e){
                        pw = "";
                    }

                    // Processing request using id and pw
                    boolean isOK = false;
                    String content = "";
                    // 회원가입
                    if (path.startsWith("/user/join")) {
                        if(ap.join(id, pw)) {
                            isOK = true;
                            content = "Signed up for " + id + " account.";
                        }
                    }
                    // 로그인
                    else if (path.startsWith("/user/login")){
                        if(ap.login(id, pw)){
                            isOK = true;
                            content = "Logged in with " + id + " account.";
                        }
                    }
                    // 로그아웃
                    else if (path.startsWith("/user/logout")){
                        if(ap.logout(id)){
                            isOK = true;
                            content = "Logged out with " + id + " account.";
                        }
                    }
                    // 회원탈퇴
                    else if (path.startsWith("/user/leave")){
                        if(ap.leave(id, pw)){
                            isOK = true;
                            content = id + " account has been deleted";
                        }
                    }
                    // 회원복구
                    else if (path.startsWith("/user/recover")){
                        if(ap.recover(id, pw)){
                            isOK = true;
                            content = id + " account has been recovered";
                        }
                    }

                    // Print only when status code is 200 OK
                    if (isOK){
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/plain; charset=utf-8");
                        out.println("Content-Length: " + content.length());
                        out.println();
                        out.println(content);
                    }
                    // When status code is not 200 OK
                    else{
                        out.println("HTTP/1.1 400 Bad Request");
                        out.println();
                    }

                }
                // Search function lists and administrator function lists
                else if(path.startsWith("/data")) {
                    int queryStartIndex = path.indexOf('?');
                    int userStartIndex = path.indexOf("&user");
                    String searchKey = path.substring(queryStartIndex + 3, userStartIndex);

                    // Processing request using id and pw
                    boolean isOK = false;
                    String content = "";
                    String id = ap.getId();
                    // 검색 기능
                    if(path.startsWith("/data/search")){
                        if(!(content=sp.search(searchKey)).equals("")){
                            isOK = true;
                        }
                    }
                    // MY 검색어 저장 기능
                    else if(path.startsWith("/data/save_data")){
                        if(sp.save(id, searchKey)){
                            isOK = true;
                            content = "Stored MY search word";
                        }
                    }
                    // MY 검색어 로드 기능
                    else if(path.startsWith("/data/load_data")){
                        if(!(content = sp.load(id)).equals("")){
                            isOK = true;
                        }
                    }
                    // 친구 검색어 로드 기능
                    else if(path.startsWith("/data/load_fri")){
                        if(!(content = sp.load(searchKey)).equals("")){
                            isOK = true;
                        }
                    }
                    // HOT 검색어 로드 기능
                    else if(path.startsWith("/data/load_hot")){
                        if(!(content = sp.loadHot(id)).equals("")){
                            isOK = true;
                        }
                    }
                    // 회원정보 로드 기능
                    else if(path.startsWith("/data/load_acc")){
                        if(!(content = ap.loadAcc()).equals("")){
                            isOK = true;
                        }
                    }
                    // 시스템 로그정보 로드 기능
                    else if(path.startsWith("/data/load_log")){
                        if(!(content = ap.loadLog()).equals("")){
                            isOK = true;
                        }
                    }

                    if (isOK){
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/plain; charset=utf-8");
                        out.println("Content-Length: " + content.length());
                        out.println();
                        out.println(content);
                    }
                    // When status code is not 200 OK
                    else{
                        out.println("HTTP/1.1 400 Bad Request");
                        out.println();
                    }
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
