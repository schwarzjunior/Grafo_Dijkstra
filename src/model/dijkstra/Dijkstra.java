package model.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.grafo.Aresta;
import model.grafo.Grafo;
import model.grafo.Vertice;

public class Dijkstra {
	private List<Aresta> arestas;
	private Set<Vertice> visitados;
	private Set<Vertice> naoVisitados;
	private Map<Vertice, Vertice> anteriores;
	private Map<Vertice, Double> distancias;

	public Dijkstra(Grafo grafo) {
		arestas = new ArrayList<Aresta>(grafo.getArestas());
		visitados = new HashSet<Vertice>();
		naoVisitados = new HashSet<Vertice>();
		anteriores = new HashMap<Vertice, Vertice>();
		distancias = new HashMap<Vertice, Double>();
	}

	public LinkedList<Vertice> executar(Vertice origem, Vertice destino) {
		naoVisitados.add(origem);
		distancias.put(origem, 0d);

		while (naoVisitados.size() > 0) {
			Vertice vertice = getMinimo(naoVisitados);
			visitados.add(vertice);
			naoVisitados.remove(vertice);
			procurarDistanciasMinimas(vertice);
		}

		if (anteriores.get(destino) == null) {
			return null;
		}

		LinkedList<Vertice> caminho = new LinkedList<Vertice>();
		Vertice passo = destino;

		caminho.add(passo);

		while (anteriores.get(passo) != null) {
			passo = anteriores.get(passo);
			caminho.add(passo);
		}

		Collections.reverse(caminho);
		return caminho;
	}

	private Vertice getMinimo(Set<Vertice> vertices) {
		Vertice minimo = null;
		for (Vertice vertice : vertices) {
			if (minimo == null || (getMenorDistancia(vertice) < getMenorDistancia(minimo))) {
				minimo = vertice;
			}
		}
		return minimo;
	}

	private double getMenorDistancia(Vertice destino) {
		Double distancia = distancias.get(destino);
		return (distancia == null ? Double.MAX_VALUE : distancia);
	}

	private void procurarDistanciasMinimas(Vertice vertice) {
		List<Vertice> verticesAdjacentes = getVizinhos(vertice);
		for (Vertice destino : verticesAdjacentes) {
			if (getMenorDistancia(destino) > (getMenorDistancia(vertice) + getDistancia(vertice, destino))) {
				distancias.put(destino, getMenorDistancia(vertice) + getDistancia(vertice, destino));
				anteriores.put(destino, vertice);
				naoVisitados.add(destino);
			}
		}
	}

	private double getDistancia(Vertice vertice, Vertice destino) {
		for (Aresta aresta : arestas) {
			if (aresta.getOrigem().equals(vertice) && aresta.getDestino().equals(destino)) {
				Double custo = (aresta.getCusto() == 0d ? 1 : aresta.getCusto() * 1000d);
				return aresta.getDistancia() * custo;
			}
		}
		throw new RuntimeException("Erro misterioso");
	}

	private List<Vertice> getVizinhos(Vertice vertice) {
		List<Vertice> vizinhos = new ArrayList<Vertice>();
		for (Aresta aresta : arestas) {
			if (aresta.getOrigem().equals(vertice) && !isVisitado(aresta.getDestino())) {
				vizinhos.add(aresta.getDestino());
			}
		}
		return vizinhos;
	}

	private boolean isVisitado(Vertice vertice) {
		return visitados.contains(vertice);
	}
}
