#!/usr/bin/env bash
set -e
PROJECT_ROOT="C:\Users\Asus\Desktop\tugas-akhir\backend\services"
OUTDIR="/tmp/placeholder_build"
mkdir -p "$OUTDIR"
cd "$OUTDIR"

cat > Main.java <<'JAVA'
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Placeholder service running...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
JAVA

javac Main.java
cat > manifest.txt <<'MAN'
Main-Class: Main

MAN
jar cfm placeholder.jar manifest.txt Main.class

SERVICES=(auth-service user-service masterdata-service procurement-service vendor-service payment-service admin-service notification-service document-service gateway-service audit-service workflow-service reporting-service)

for s in "${SERVICES[@]}"; do
  mkdir -p "$PROJECT_ROOT/services/$s"
  cp placeholder.jar "$PROJECT_ROOT/services/$s/placeholder.jar"
  echo "copied placeholder.jar -> $PROJECT_ROOT/services/$s/placeholder.jar"
done

echo "Done. Placeholder jars copied."
