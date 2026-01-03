//package ;

import java.io.*;
import javax.microedition.io.*;

class TunnelD {
    
    String pass = "i forgor :skull:";
    
    private static final String aesKey = "aesEncryptionKey"; // 256 bit key
    private static final String aesInitVector = "fds$89fK-m}@cp09"; // 16 bytes IV
    
    TunnelD() { }
    
    public String sendResp(String req) throws Exception{
        String host = new String(SettingsC.getRecord(SettingsC.IP_ID), "UTF-8");
        SocketConnection sc = (SocketConnection) Connector.open("socket://" + host +";deviceside=true;interface=wifi");

        InputStream is = sc.openInputStream();
        
        OutputStream os = sc.openOutputStream();

        StringBuffer s_temp = new StringBuffer();

        os.write(password(req).getBytes("UTF-8"));
        os.flush();
        
        int ch = 0;
        while(ch != -1 && ch != '\n'){
            ch = is.read();
            s_temp.append((char) ch);
        }

        is.close();
        os.close();
        sc.close();
        
        return new String( s_temp.toString().getBytes(), "UTF-8");
    }
    
    public void send(String req) throws Exception{
        String host = new String(SettingsC.getRecord(SettingsC.IP_ID), "UTF-8");
        SocketConnection sc = (SocketConnection) Connector.open("socket://" + host +";deviceside=true;interface=wifi");

        OutputStream os = sc.openOutputStream();

        os.write(password(req).getBytes("UTF-8"));
        os.flush();

        os.close();
        sc.close();
    }
    
    private String password(String s){
        return "{" + s + ", \"password\" : \"" + encrypt(pass) + "\"}";
    }
} 
