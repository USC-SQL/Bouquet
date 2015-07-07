package usc.edu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dingli on 6/19/15.
 */
public class BundledResponse {
    String link;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    int anchorpoint=0;
    public BundledResponse(byte[] content) throws IOException {
        for(int i=0;i<content.length-2;i++)
        {
            if(content[i]=='#'&&content[i+1]=='#'&&content[i+2]=='#')
            {
                anchorpoint=i;
                break;
            }
        }
        //System.out.println("reanchor:"+anchorpoint);
        int length=content.length-(anchorpoint+3);
        this.link=new String(content,0,anchorpoint);
        buffer.write(content,anchorpoint+3,length);
        buffer.close();
    }
}
