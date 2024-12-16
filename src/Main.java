import java.io.*;
import java.util.*;

public class Main {

    public static void ProcuraPorNome(List<String> filmes, String nome){


        boolean encontrado = false;
        for (String filme : filmes) {
            if (filme.contains(nome)) {
                encontrado = true;
                break;
            }
        }
        if (encontrado) {
            System.out.println("Filme encontrado - " + nome);
        }
        else{
            System.out.println("Filme não encontrado - " + nome);
        }
    }

    private static class LeitorDeArquivo implements Runnable {

        private final String nomeDoArquivo;
        private final List<String> filmes;

        public LeitorDeArquivo(String nomeDoArquivo, List<String> filmes) {
            this.nomeDoArquivo = nomeDoArquivo;
            this.filmes = filmes;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    filmes.add(linha);
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        final String nomeDoArquivo = "src/movies.txt";

        List<String> filmes = Collections.synchronizedList(new ArrayList<>());

        Thread threadDeLeitura = new Thread(new LeitorDeArquivo(nomeDoArquivo, filmes));
        threadDeLeitura.start();

        try {
            threadDeLeitura.join();
        } catch (InterruptedException e) {
            System.err.println("A thread foi interrompida: " + e.getMessage());
        }

        System.out.println("Lista de Filmes:");
        for (String filme : filmes) {
            System.out.println(filme);
        }
        String nome = "Toy,";
        ProcuraPorNome(filmes, nome);
    }
}
