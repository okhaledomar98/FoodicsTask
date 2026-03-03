package utils;

public class TestContext {
    private static ThreadLocal<String> sharedUserId = new ThreadLocal<>();
    private static ThreadLocal<String> sharedUserName = new ThreadLocal<>();
    private static ThreadLocal<String> sharedUserJob = new ThreadLocal<>();

    // ID
    public static void setUserId(String id) { sharedUserId.set(id); }
    public static String getUserId() { return sharedUserId.get(); }

    // Name
    public static void setUserName(String name) { sharedUserName.set(name); }
    public static String getUserName() { return sharedUserName.get(); }

    // Job
    public static void setUserJob(String job) { sharedUserJob.set(job); }
    public static String getUserJob() { return sharedUserJob.get(); }

    public static void unload() {
        sharedUserId.remove();
        sharedUserName.remove();
        sharedUserJob.remove();
    }
}