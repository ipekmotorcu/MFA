public class Admin {
    private int adminId;
    private String adminName;

    public Admin(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
    }


    public void createMusic(String musicName, int albumId, double duration, String category, boolean explicit) {
        // Implementation to insert music into the database
    }


    public void deleteMusic(int musicId) {
        // Implementation to delete music from the database
    }


    public void viewStatistics() {
        // Implementation to view music statistics
    }

    public void createUser(String username, int age) {
        // Implementation to add a new user
    }

    public void deleteUser(int userId) {
        // Implementation to delete a user
    }
}
