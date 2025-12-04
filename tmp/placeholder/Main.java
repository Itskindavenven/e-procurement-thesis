public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Placeholder service running...");
        // keep process alive so container doesn't exit
        Thread.sleep(Long.MAX_VALUE);
    }
}
