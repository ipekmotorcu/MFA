public interface MusicManageable {

        void createMusic(String musicName, int albumId, double duration, String category, boolean explicit);
        void deleteMusic(int musicId);
        void viewStatistics();


}
