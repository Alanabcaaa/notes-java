import java.util.*;
import java.time.Instant;

/**
 * NotesApp - Aplicativo simples de anotações em Java
 * Mostra domínio de conceitos como:
 * - Programação orientada a objetos (OOP)
 * - Coleções (List, Map)
 * - CRUD (Create, Read, Update, Delete)
 * - Entrada e saída de dados
 * 
 * Autor: SEU_NOME_AQUI
 */
public class NotesApp {

    // ======= Modelo =======
    static class Note {
        private final String id;
        private String title;
        private String body;
        private final Instant createdAt;
        private Instant updatedAt;

        public Note(String title, String body) {
            this.id = UUID.randomUUID().toString();
            this.title = Objects.requireNonNull(title);
            this.body = Objects.requireNonNull(body);
            this.createdAt = Instant.now();
            this.updatedAt = createdAt;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getBody() { return body; }

        public void setTitle(String title) {
            this.title = Objects.requireNonNull(title);
            this.updatedAt = Instant.now();
        }

        public void setBody(String body) {
            this.body = Objects.requireNonNull(body);
            this.updatedAt = Instant.now();
        }

        @Override
        public String toString() {
            return String.format(
                "\nID: %s\nTítulo: %s\nCorpo: %s\nCriado em: %s\nAtualizado em: %s\n",
                id, title, body, createdAt, updatedAt
            );
        }
    }

    // ======= Serviço =======
    static class NoteService {
        private final Map<String, Note> store = new LinkedHashMap<>();

        public Note create(String title, String body) {
            Note n = new Note(title, body);
            store.put(n.getId(), n);
            return n;
        }

        public List<Note> listAll() {
            return new ArrayList<>(store.values());
        }

        public Note findById(String id) {
            return store.get(id);
        }

        public boolean update(String id, String title, String body) {
            Note n = store.get(id);
            if (n == null) return false;
            n.setTitle(title);
            n.setBody(body);
            return true;
        }

        public boolean delete(String id) {
            return store.remove(id) != null;
        }

        public List<Note> search(String query) {
            if (query == null || query.isBlank()) return listAll();
            String q = query.toLowerCase(Locale.ROOT);
            List<Note> result = new ArrayList<>();
            for (Note n : store.values()) {
                if (n.getTitle().toLowerCase(Locale.ROOT).contains(q)
                    || n.getBody().toLowerCase(Locale.ROOT).contains(q)) {
                    result.add(n);
                }
            }
            return result;
        }
    }

    // ======= Programa principal =======
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NoteService service = new NoteService();

        service.create("Boas-vindas", "Este é um projeto Java simples para portfolio.");
        service.create("Exemplo", "Você pode criar, listar e buscar notas.");

        System.out.println("=== NotesApp (Java Portfolio) ===");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Listar notas");
            System.out.println("2) Criar nova nota");
            System.out.println("3) Atualizar nota");
            System.out.println("4) Deletar nota");
            System.out.println("5) Buscar notas");
            System.out.println("0) Sair");
            System.out.print("Escolha: ");
            String opt = sc.nextLine();

            switch (opt) {
                case "1" -> list(service);
                case "2" -> create(sc, service);
                case "3" -> update(sc, service);
                case "4" -> delete(sc, service);
                case "5" -> search(sc, service);
                case "0" -> {
                    System.out.println("Encerrando... Até logo!");
                    return;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // ======= Métodos auxiliares =======
    private static void list(NoteService service) {
        List<Note> notes = service.listAll();
        if (notes.isEmpty()) System.out.println("Nenhuma nota encontrada.");
        else notes.forEach(System.out::println);
    }

    private static void create(Scanner sc, NoteService service) {
        System.out.print("Título: ");
        String title = sc.nextLine();
        System.out.print("Corpo: ");
        String body = sc.nextLine();
        Note n = service.create(title, body);
        System.out.println("Nota criada com ID: " + n.getId());
    }

    private static void update(Scanner sc, NoteService service) {
        System.out.print("ID da nota: ");
        String id = sc.nextLine();
        System.out.print("Novo título: ");
        String title = sc.nextLine();
        System.out.print("Novo corpo: ");
        String body = sc.nextLine();
        boolean ok = service.update(id, title, body);
        System.out.println(ok ? "Nota atualizada!" : "Nota não encontrada!");
    }

    private static void delete(Scanner sc, NoteService service) {
        System.out.print("ID da nota: ");
        String id = sc.nextLine();
        boolean ok = service.delete(id);
        System.out.println(ok ? "Nota removida!" : "Nota não encontrada!");
    }

    private static void search(Scanner sc, NoteService service) {
        System.out.print("Digite sua busca: ");
        String query = sc.nextLine();
        List<Note> results = service.search(query);
        if (results.isEmpty()) System.out.println("Nenhuma nota encontrada.");
        else results.forEach(System.out::println);
    }
}
