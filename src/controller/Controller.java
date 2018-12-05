package controller;

import java.util.LinkedList;
import java.util.Scanner;

import model.dijkstra.Dijkstra;
import model.grafo.Grafo;
import model.grafo.Vertice;
import persistencia.LeitorArquivoXML;

public class Controller {
	public static void main(String[] args) {

		LeitorArquivoXML leitor = new LeitorArquivoXML();
		leitor.fazerParsing();

		System.out.println("Qtd Vertices: " + leitor.grafo.getVertices().size());
		System.out.println("Qtd Arestas: " + leitor.grafo.getArestas().size());

//		System.exit(0);
//
//		Grafo grafo = LeitorArquivoGrafo.lerArquivoGrafo();
		Grafo grafo = LeitorArquivoXML.grafo;

		Scanner scanner = new Scanner(System.in);

		System.out.println("ID do Vertice de origem: ");
		int origem = scanner.nextInt();
		System.out.println("ID do Vertice de destino: ");
		int destino = scanner.nextInt();

		scanner.close();

		Dijkstra dijkstra = new Dijkstra(grafo);
		LinkedList<Vertice> caminho = dijkstra.executar(grafo.getVerticeById(origem), grafo.getVerticeById(destino));

		System.out.println("");
		if (caminho == null) {
			System.out.println("Rota inexistente!");
		} else {
			int passo = 0;
			for (Vertice v : caminho) {
				passo++;
				System.out.println("Passo " + passo + " -> " + v.getId());
			}
		}
	}
}
