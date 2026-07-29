public class HashGenerator {
    public static void main(String[] args) throws Exception {
        String password = "nperera";
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        System.out.println(sb.toString());
    }
}