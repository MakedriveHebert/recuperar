import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firestore.admin.v1.FirestoreAdminClient;
import com.google.firestore.admin.v1.RestoreDatabaseMetadata;
import com.google.firestore.admin.v1.RestoreDatabaseRequest;
import com.google.firestore.v1.DatabaseName;

// Inicialize o cliente Admin
FirestoreAdminClient firestoreAdminClient = FirestoreAdminClient.create();

// Defina o momento antes da exclusão dos dados
Timestamp timestamp = Timestamp.parseTimestamp("2025-04-02T18:47:00Z"); // Ajuste para o momento correto

// Nome do projeto e banco de dados
String projectId = "makeduka";
String databaseId = "(default)";

// Caminho completo do banco
String databasePath = 
    String.format("projects/%s/databases/%s", projectId, databaseId);

// Execute a recuperação
RestoreDatabaseRequest request = RestoreDatabaseRequest.newBuilder()
    .setParent("projects/" + projectId)
    .setDatabaseId(databaseId)
    .setBackup("projects/" + projectId + 
        "/locations/LOCATION_ID/backups/BACKUP_ID") // Se estiver restaurando de um backup
    // OU use o timestamp para PITR:
    .setTimestamp(com.google.protobuf.Timestamp.newBuilder()
        .setSeconds(timestamp.getSeconds())
        .setNanos(timestamp.getNanos())
        .build())
    .build();

// Inicie a operação de restauração
RestoreDatabaseMetadata metadata = 
    firestoreAdminClient.restoreDatabaseAsync(request).get();