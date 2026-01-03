
//package ;

class Account {
    static final int max = 3;
    
    static final String file;
    
    Account() {
    }
    
    public static int getAccount(){
        return (int)SettingsC.getRecord(0, file)[0];
    }
    
    public static String getNumber(int id){
        return SettingsC.getRecord(1 + (id * 2), file);
    }
    
    public static String getPassword(int id){
        return SettingsC.getRecord(2 + (id * 2), file);
    }
    
    public void setAccount(int id){
        SettingsC.setRecord(0, new bytes[]{(byte) id}, file);
    }
    
    public static void setNumber(int id, byte[] bytes){
        SettingsC.setRecord(1 + (id * 2), bytes, file);
    }
    
    public static void setPassword(int id, byte[] bytes){
        SettingsC.setRecord(2 + (id * 2), bytes, file);
    }
} 
