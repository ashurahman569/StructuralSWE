// A legacy system uses a database that only supports SQL queries. However, a new
// requirement necessitates the use of NoSQL databases for better scalability and flexibility.
// The challenge is to integrate the new NoSQL database without modifying the existing
// SQL-based code. Provide a solution with an appropriate design pattern to seamlessly
// integrate the NoSQL database. Provide driver code to demonstrate the integration.

interface DatabaseQuery {

    void executeQuery(String query);
}

class SQLDatabase implements DatabaseQuery {

    @Override
    public void executeQuery(String sqlQuery) {
        System.out.println("Executing SQL query: " + sqlQuery);
    }
}

class NoSQLDatabaseAdapter implements DatabaseQuery {

    private NoSQLDatabase noSQLDatabase;

    public NoSQLDatabaseAdapter(NoSQLDatabase noSQLDatabase) {
        this.noSQLDatabase = noSQLDatabase;
    }

    @Override
    public void executeQuery(String query) {
        // Here we can add logic to convert SQL queries to NoSQL queries if needed
        noSQLDatabase.runQuery(query);
    }
}

class NoSQLDatabase {

    public void runQuery(String noSQLQuery) {
        System.out.println("Executing NoSQL query: " + noSQLQuery);
    }
}
// ─────────────────────────────────────────────────
// Application Layer — works ONLY with DatabaseQuery
// (simulates the existing SQL-based legacy code)
// ─────────────────────────────────────────────────
class Application {
    private DatabaseQuery database;

    // Accepts ANY DatabaseQuery — SQL or adapted NoSQL
    public Application(DatabaseQuery database) {
        this.database = database;
    }

    public void fetchUsers() {
        database.executeQuery("SELECT * FROM users");
    }

    public void insertOrder(String item) {
        database.executeQuery("INSERT INTO orders (item) VALUES ('" + item + "')");
    }

    public void deleteRecord(int id) {
        database.executeQuery("DELETE FROM records WHERE id = " + id);
    }

    public void updateStock(String product, int qty) {
        database.executeQuery(
            "UPDATE stock SET quantity = " + qty +
            " WHERE product = '" + product + "'"
        );
    }
}
public class Online21C2 {
    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("   Legacy DB Integration — Adapter Pattern    ");
        System.out.println("==============================================\n");

        // ── 1. Original SQL Database (unchanged behaviour) ──
        System.out.println("--- Running on SQL Database ---");
        DatabaseQuery sqlDB = new SQLDatabase();
        Application sqlApp  = new Application(sqlDB);

        sqlApp.fetchUsers();
        sqlApp.insertOrder("Laptop");
        sqlApp.deleteRecord(42);
        sqlApp.updateStock("Keyboard", 100);

        System.out.println();

        // ── 2. NoSQL Database via Adapter ──
        //    Application code is IDENTICAL — zero changes!
        System.out.println("--- Running on NoSQL Database (via Adapter) ---");
        NoSQLDatabase  noSQLDB    = new NoSQLDatabase();
        DatabaseQuery  adaptedDB  = new NoSQLDatabaseAdapter(noSQLDB);
        Application    noSQLApp   = new Application(adaptedDB);

        noSQLApp.fetchUsers();
        noSQLApp.insertOrder("Laptop");
        noSQLApp.deleteRecord(42);
        noSQLApp.updateStock("Keyboard", 100);

        System.out.println();

        // ── 3. Runtime switch — swap DB without changing Application ──
        System.out.println("--- Direct Adapter usage (without Application wrapper) ---");
        DatabaseQuery db1 = new SQLDatabase();
        DatabaseQuery db2 = new NoSQLDatabaseAdapter(new NoSQLDatabase());

        DatabaseQuery[] databases = { db1, db2 };

        for (DatabaseQuery db : databases) {
            System.out.println("  Using: " + db.getClass().getSimpleName());
            db.executeQuery("SELECT * FROM products");
            System.out.println();
        }
    }
}
