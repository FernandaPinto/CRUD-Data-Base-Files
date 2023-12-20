import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Header<T> {  //generic class to help the control of bytes conversion

    protected int header;
    protected char grave;

    T object;

    public Header(T obj){
        this.object = obj;
    }

    public byte[] toByteArray() throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        if(object instanceof Integer){  //check type of object and return int or char to a byte array
            dos.writeInt((int)object);
        }else{
            dos.writeChar((char)object); 
        }
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
       
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        header = dis.readInt();
        grave = dis.readChar();
    }
    
}
