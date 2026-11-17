public class User {

    private final String username;
    private final String password;

    private int failedAttempts = 0;
    private long blockedAt = 0;

    public User(String username, String password) {
        validUsername(username);
        validPassword(password);

        this.username = username;
        this.password = password;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public synchronized boolean isBlocked(int blockSeconds) {
        if (blockedAt == 0) {
            return false;
        }

        long elapsed = System.currentTimeMillis() - blockedAt;
        long duration = blockSeconds * 1000L;

        if (elapsed >= duration) {
            failedAttempts = 0;
            blockedAt = 0;
            return false;
        }

        return true;
    }

    public synchronized boolean registerFailedAttempt(
            int maxAttempts, int blockSeconds) {

        if (isBlocked(blockSeconds)) {
            return true;
        }

        failedAttempts++;

        if (failedAttempts >= maxAttempts) {
            blockedAt = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    private void validUsername(String user) {
        if (user.length() > 50) {
            throw new IllegalArgumentException(
                "Username is too long, try something shorter"
            );
        }

        int indexOfShto = user.indexOf('@');
        int indexOfDot = user.lastIndexOf('.');

        if (indexOfShto <= 0
                || indexOfDot <= indexOfShto + 1
                || indexOfDot == user.length() - 1) {
            throw new IllegalArgumentException(
                "Please enter a valid Email as username"
            );
        }

        String firstPart = user.substring(0, indexOfShto);
        String secondPart = user.substring(indexOfShto + 1, indexOfDot);
        String thirdPart = user.substring(indexOfDot + 1);

        if (!checkValidParts(firstPart, secondPart, thirdPart)) {
            throw new IllegalArgumentException(
                "Please enter a valid Email as username"
            );
        }
    }

    private void validPassword(String pass) {
        if (pass.length() < 8) {
            throw new IllegalArgumentException(
                "Your password is too short, add more characters"
            );
        }

        if (pass.length() > 12) {
            throw new IllegalArgumentException(
                "Your password is too long, try a shorter one"
            );
        }

        boolean validCharacters = pass.matches("[A-Za-z0-9!@#$%^&*()_+]+");
        boolean hasLetter = pass.matches(".*[A-Za-z].*");
        boolean hasDigit = pass.matches(".*[0-9].*");
        boolean hasSpecial = pass.matches(".*[!@#$%^&*()_+].*");

        if (!validCharacters || !hasLetter || !hasDigit || !hasSpecial) {
            throw new IllegalArgumentException(
                "Please enter a valid password"
            );
        }
    }

    private boolean checkValidParts(
            String firstPart, String secondPart, String thirdPart) {

        return firstPart.matches("[A-Za-z0-9._%+-]+")
            && secondPart.matches("[A-Za-z0-9][A-Za-z0-9.-]*")
            && thirdPart.matches("[A-Za-z]{2,}");
    }
}