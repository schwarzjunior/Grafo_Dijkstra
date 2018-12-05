package persistencia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.grafo.Grafo;
import model.grafo.Vertice;

public class LeitorArquivoXML {
	private static final String arqDados = "dados_grafo.xml";
	private static final String TAG_GRAFO = "grafo";
	private static final String TAG_VERTICES = "vertices";
	private static final String TAG_ARESTAS = "arestas";

	public static Grafo grafo;

	public LeitorArquivoXML() {
		grafo = new Grafo();
	}

	public void fazerParsing() {
		Document doc = getDocumentXML();
		if (doc == null) {
			System.out.println("ERRO: Erro ao abrir/ler o arquivo XML.");
			return;
		}

		Node nodeGrafo = getNodeByName(doc, TAG_GRAFO);

		// obtendo o node <vertices>
		Node nodeVertices = getNodeByName(nodeGrafo, TAG_VERTICES);
		if (nodeVertices == null) {
			System.out.println("\nERRO: Elemento (node) <vertices> nao foi encontrado no arquivo XML.\n");
		}

		// obtendo o node <arestas>
		Node nodeArestas = getNodeByName(nodeGrafo, TAG_ARESTAS);
		if (nodeArestas == null) {
			System.out.println("\nERRO: Elemento (node) <arestas> nao foi encontrado no arquivo XML.\n");
		}

		// obtem a lista de elementos <item> contidos em <vertices>
		List<Element> itensVertices = getChildElementsByTagName(nodeVertices, "item");
		for (int i = 0; i < itensVertices.size(); i++) {
			Element curElement = itensVertices.get(i);
			Vertice newVertice = new Vertice(Integer.parseInt(curElement.getAttribute("id")),
					Double.parseDouble(curElement.getAttribute("latitude")),
					Double.parseDouble(curElement.getAttribute("longitude")), curElement.getAttribute("descricao"));
			grafo.insertVertice(newVertice);
		}

		// obtem a lista de elementos <item> contidos em <arestas>
		List<Element> itensArestas = getChildElementsByTagName(nodeArestas, "item");
		for (int i = 0; i < itensArestas.size(); i++) {
			Element curElement = itensArestas.get(i);
			grafo.insertAresta(Integer.parseInt(curElement.getAttribute("origem")),
					Integer.parseInt(curElement.getAttribute("destino")),
					Double.parseDouble(curElement.getAttribute("custo")));
		}
	}

	private List<Element> getChildElementsByTagName(Node rootNode, String childNodeName) {
		// objeto List<Element> que sera retornado pelo metodo
		List<Element> retList = new ArrayList<Element>();

		if (rootNode.hasChildNodes()) {
			NodeList nodeList = rootNode.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				// obtem o node atual
				Node curNode = nodeList.item(i);
				// certificando-se de que eh um ELEMENT_NODE
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					// verificando se eh o nome de node procurado
					if (curNode.getNodeName() == childNodeName) {
						Element curElement = (Element) curNode;
						retList.add(curElement);
					}
				}
			}
		}

		return retList;
	}

	/**
	 * Percorre e procura, na arvore XML, por um determinado elemento (Node).
	 * 
	 * @param rootNode Obtejo Node que sera percorrido a procura do elemento.
	 * @param nodeName Nome do elemento (Node) pesquisado.
	 * @return
	 */
	private Node getNodeByName(Node rootNode, String nodeName) {
		// objeto Node que sera retornado pelo metodo
		Node retNode = null;

		if (rootNode.hasChildNodes()) {
			NodeList nodeList = rootNode.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				// obtem o node atual
				Node curNode = nodeList.item(i);
				// certificando-se de que eh um ELEMENT_NODE
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					if (curNode.getNodeName() == nodeName) {
						retNode = curNode;
						break;
					}
				}
			}
		}

		return retNode;
	}

	private Node getNodeByName(Document doc, String nodeName) {
		Node retNode = null;

		if (doc.hasChildNodes()) {
			NodeList nodeList = doc.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node curNode = nodeList.item(i);
				if (curNode.getNodeType() == Node.ELEMENT_NODE) {
					if (curNode.getNodeName() == TAG_GRAFO) {
						retNode = curNode;
						break;
					}
				}
			}
		}

		return retNode;

//		return getNodeByName(doc, nodeName);
	}

	private Document getDocumentXML() {
		File xmlFile = null;
		DocumentBuilderFactory docBuilderFactory = null;
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try {
			xmlFile = new File(arqDados);
			docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.parse(xmlFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
}
