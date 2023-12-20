import java.io.*;

public class App {

    public static void main(String[] args) throws Exception {
          
        Entity[] temp = new Entity[100]; //classes with information about the data
      
        String line = "";  
        String splitBy = ","; 
        RandomAccessFile arq;
        
        byte[] ba;

        int index = 0;
        int indexHeader = 0;
        int id=0;

        try {
            
            arq = new RandomAccessFile("entity.db", "rw");

            //parsing a CSV file into BufferedReader class constructor  
            BufferedReader br = new BufferedReader(new FileReader("C:\\path\\demo.csv"));  
            
            Header<Integer> h = new Header<>(indexHeader); //create header at the beginning of the arrary of bytes
            ba = h.toByteArray();
            arq.write(ba);

            while ((line = br.readLine()) != null)   //returns a Boolean value  
            {  
                String[] employee = line.split(splitBy); // use comma as separator  
                String[] dataTemp = (employee[1].split("-"));  //slip date by -

                //add to the new object Entity 
                temp[index] = new Entity(Integer.parseInt(employee[0]), Integer.parseInt(dataTemp[0] + dataTemp[1] + dataTemp[2]), employee[2], employee[3], employee[4], employee[5], employee[6], employee[7], employee[8], Short.parseShort(employee[9]), employee[10], employee[11], employee[12],employee[13]);
   
                ba = temp[index].toByteArray(true); //set bytes arra with the information
                arq.writeInt(ba.length);
                arq.write(ba);  //write on the file

                index++;
                id = Integer.parseInt(employee[0]); //get last id to set in the header
            }
            
            long lastPointer = arq.getFilePointer();  //get last position pointer
            
            //update the header with ID
            arq.seek(0); 
            Header<Integer> h2 = new Header<>(id);        
            ba = h2.toByteArray();
            arq.write(ba);

            arq.seek(lastPointer); //set pointer to the last position of the file 
            
            br.close();
            arq.close();
            
            CRUD crud = new CRUD(); //new object CRUD
            crud.Initialize(index, lastPointer);

            crud.Create(22,19970827,"no","horse","shoot gun","dallas","usa","texas","nanda gamer",(short)9,"female","w","true","yes");
            
            crud.Read(137);
            
            boolean update = crud.Update(137,19970827,"no","boat","arrow","austin","usa","texas","nanda gomr",(short)9,"female","w","true","yes");
            if(update == true){
                System.out.println("Register updated");
            }else {
                System.out.println("Fail to updated the register");
            }

            crud.Read(137);

            boolean deleted = crud.Delete(133);

            if(deleted == true){
                System.out.println("Register deleted");
            }else {
                System.out.println("Fail to deleted the register");
            }
            
            crud.Read(133);

            crud.Create(900,01102003,"kill with rock","car","knife","las vegas","usa","nevada","Elvis the pelvis",(short)35,"male","w","no","no");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
