import java.io.IOException;
import java.io.RandomAccessFile;

public class CRUD {

    protected RandomAccessFile arq;

    Entity tempEntity;
    
    byte[] b;

    long pointer;

    int indexHeader;
    int registerCount;

    public void Initialize(int s, long p) throws IOException {
        pointer = p;
        registerCount = s;
    }

    int ReadHeader(int numberToAdd){
        
        int idHeader=0;
        
        try {

            if(numberToAdd != 0){
                
                arq.seek(0);                  // move pointer to the header 
            
                idHeader = arq.readInt();      // read id from file 

                arq.seek(0);              // move pointer to the header 
                
                idHeader += numberToAdd; 
                Header<Integer> a = new Header<>(idHeader);
                b = a.toByteArray();

                arq.write(b);                       //update header value 

            }else{

                arq.seek(0);                  // move pointer to the header 
            
                idHeader = arq.readInt();      // read id from file 

                arq.seek(0);              // move pointer to the header 

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return idHeader;
    }

    public void Create(int Id, int Date, String threat_type, String Flee, String Warmed, String City, String Country, String State, String Name,  short Age, String Gender, String Race, String Mental, String Body_cam){

        try {

            arq = new RandomAccessFile("entity.db", "rw");

            int temp = ReadHeader(1);

            tempEntity = new Entity(temp, Date, threat_type, Flee, Warmed, City, Country, State, Name, Age, Gender, Race, Mental, Body_cam);  //create new register

            arq.seek(pointer);               //move pointer to the last position

            b = tempEntity.toByteArray(true);
            arq.writeInt(b.length);
            arq.write(b);                 //write new register

            pointer += b.length;         //get last position pointer + current register size
            registerCount++;
  
            arq.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Read(int id){
        
        boolean find = false;
        try {
            
            arq = new RandomAccessFile("entity.db", "r");
          
            arq.seek(4);

            int tempH = registerCount;

            while(tempH > 0){

                int sizeRegister = arq.readInt(); //keep the size of the current register
                char isDeleted = arq.readChar();
                
                if(isDeleted == '%'){ //not deleted

                    if(arq.readInt() == id){ //current number compare with the id
                        
                        arq.seek(arq.getFilePointer()-4);  //send pointer to the begginig of the register (after the grave)
                      
                        System.out.print("Find register: ");
                        
                            tempEntity = new Entity();
                            b = new byte[sizeRegister];
                            arq.read(b);
                            tempEntity.fromByteArray(b);

                        System.out.println(tempEntity);

                        arq.seek(arq.getFilePointer()+4); //send pointer to the previus position (after the id)

                        find = true;
                        break;
                    }
                }else{               
                    arq.seek(arq.getFilePointer()+4); 
                }

                long nextRegister = (sizeRegister-6) + arq.getFilePointer(); //get the total of bytes - 6 and add the current pos
                arq.seek(nextRegister);

                tempH--;
            }
            
            
            if(find == false){
                System.out.print("Register not found in the data base");
            }

            arq.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean Update(int id, int Date, String threat_type, String Flee, String Warmed, String City, String Country, String State, String Name,  short Age, String Gender, String Race, String Mental, String Body_cam){

        boolean find = false;
        try {
            
            arq = new RandomAccessFile("entity.db", "rw");

            arq.seek(4);

            int tempH = registerCount;    

            while(tempH > 0){

                int sizeRegister = arq.readInt(); //keep the size of the current register
                char isDeleted = arq.readChar();
                
                if(isDeleted == '%'){ //not deleted

                    int currentId = arq.readInt();

                    if(currentId == id){ //current number compare with the id
                        
                        tempEntity = new Entity(currentId, Date, threat_type, Flee, Warmed, City, Country, State, Name, Age, Gender, Race, Mental, Body_cam);  //create new register
                        b = tempEntity.toByteArray(true);  

                         
                        arq.seek(arq.getFilePointer()-4);  //send pointer to the begginig of the register (size of the register)

                        if(b.length <= sizeRegister){
                            arq.seek(arq.getFilePointer()-2); 
                            arq.write(b);            
                        }else{
                           
                            arq.seek(arq.getFilePointer()-2); //grave position
                            
                            Header<Character> a = new Header<>('#'); //instance new header generic to set grave to true(#)
                            byte[] ba;
                            ba = a.toByteArray();
                            arq.write(ba);
                            
                            arq.seek(arq.length()-1); //end of the file to record the new register
                            arq.writeInt(b.length);
                            arq.write(b);

                            registerCount++;
                            break;

                        }

                        arq.seek(arq.getFilePointer()+4); //send pointer to the previus position (after the id)

                        find = true;
                        break;
                    }
                }

                long nextRegister = (sizeRegister-6) + arq.getFilePointer(); //get the total of bytes - 6 and add the current pos
                arq.seek(nextRegister);

                tempH--;
            }
            
            arq.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return find;
    }
    
    public boolean Delete(int id){
        
        boolean find = false;
        try {
            
            arq = new RandomAccessFile("entity.db", "rw");

            arq.seek(4);
            
            int tempH = registerCount;    

            while(tempH > 0){

                int sizeRegister = arq.readInt(); //keep the size of the current register
                char isDeleted = arq.readChar();
                
                if(isDeleted == '%'){ //not deleted

                    int currentId = arq.readInt();

                    if(currentId == id){ //current number compare with the id

                        arq.seek(arq.getFilePointer()-6); //grave position
                            
                        Header<Character> a = new Header<>('#'); //instance new header generic to set grave to true(#)
                        byte[] ba;
                        ba = a.toByteArray();
                        arq.write(ba);  

                        registerCount--;  

                        arq.seek(arq.getFilePointer()+6); //send pointer to the previus position (after the id)

                        find = true;
                        break;
                    }
                }

                long nextRegister = (sizeRegister-6) + arq.getFilePointer(); //get the total of bytes - 6 and add the current pos
                arq.seek(nextRegister);

                tempH--;
            }
            
            arq.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return find;
    }
}
