
//package ;

import javax.microedition.lcdui.*;

class StringD {
    
    public StringD() {    }
    
    public static String[] cropString(String s, Font font, int max_x, int size){
        if(s.length() == 0){
            return new String[]{s};
        }
        else{
            String[] l_temp;
            String s_temp = s;
            
            l_temp = new String[size];
            
            int i;
            int j;
            
            for(j = 0; j < size && s_temp.length() > 0; j++){
                char c = 0;
                for(i = 0; i < s_temp.length() && font.substringWidth(s_temp, 0, i) < max_x && c != '\n'; i++){
                    c = s_temp.charAt(i);
                }
    
                l_temp[j] = s_temp.substring(0, i);
                s_temp = s_temp.substring(i);
            }
            
            String[] resp = new String[j];
            
            for(i = 0; i < j; i++){
                resp[i] = l_temp[i];
            }
            
            if(s_temp.length() > 0)
                resp[j - 1] = "...\n";
            
            return resp;
        }
    }
    
    public static String getFromJSON(String json, String item){
        json = json.substring(0, json.length() - 1);
        String resp = "";
        
        char c;
        char cA = 0;
        
        int start = -1;
        int end = -1;
        
        int deep = 0;
        int deepB = 0;
        
        int i;
        
        for(i = 0; i < json.length(); i++){
            c = json.charAt(i);
            
            if(start != -1 && end == -1){
                resp += c;
            }
            
            if(cA == 0){
                if(c == '\'' || c == '"' || c == '`'){
                    cA = c;
                }
                else if( c == ',' || i == 0){
                    if(deep == 0 && deepB == 0){
                        for(i++; json.charAt(i) == ' '; i++);
                        
                        if(start == -1){
                            String s_temp = json.substring(i, i + item.length() + 1);
                            
                            if(s_temp.equals(item + " ") || s_temp.equals(item + ":")){
                                for(; json.charAt(i) != ':'; i++);
                                start = i;
                            }
                        }
                        else if(end == -1){
                            end = i - 1;
                        }
                        i--;
                    }
                }
                else if(c == '{'){
                    deep++;
                }
                else if(c == '}'){
                    deep--;
                }
                else if(c == '['){
                    deepB++;
                }
                else if(c == ']'){
                    deepB--;
                }
                else if(c == '\\'){
                    i++;
                }
            }
            else {
                if(c == cA){
                    cA = 0;
                }
                else if(c == '\\'){
                    i++;
                    if(start != -1 && end == -1){
                        c = json.charAt(i);
                        if(c == 'n')
                            resp += '\n';
                        else
                            resp += c;
                    }
                }
            }
        }
        
        if(resp.length() > 0){
            for(i = 0; resp.charAt(i) == ' ' || resp.charAt(i) == ':'; i++);
            
            resp = resp.substring(i, resp.length() - 1);
            
            cA = resp.charAt(0);
            if(cA == '\'' || cA == '"' || cA == '`')
                resp = resp.substring(1, resp.length() - 1);
            
            //if(cA == '\'' || cA == '"' || cA == '`')
        }
        return resp;
    }
    
    public static String safe(String s){
        String resp = s;
        
        char c;
        
        for(int i = 0; i < resp.length(); i++){
            c = resp.charAt(i);
            if(c == '"'){
                String s_1 = resp.substring(0, i);
                String s_2 = resp.substring(i, resp.length());
                resp = s_1 + '\\' + s_2;
                i++;
            }
        }
        
        return resp;
    }
}

