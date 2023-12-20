import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Entity {
    
    protected int id;  //variables for a specific database entity
    protected int date;
    protected String threat_type;
    protected String flee_status;
    protected String warmed_with;
    protected String city;
    protected String country;
    protected String state;
    protected String name;
    protected short age;
    protected String gender;
    protected String race;
    protected String mental_ilness;
    protected String body_camera;

    //variables to handle the file
    protected int sizeEntity;
    protected char grave = '%';

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    //constructor
    public Entity(int Id, int Date, String threat_type, String Flee , String Warmed, String City, String Country, String State, String Name,  short Age, String Gender, String Race, String Mental, String Body_cam){
        this.id = Id;
        this.date = Date;
        this.threat_type = threat_type;
        this.age = Age;
        this.flee_status = Flee;
        this.warmed_with = Warmed;
        this.city = City;
        this.country = Country;
        this.state = State;
        this.name = Name;
        this.gender = Gender;
        this.race = Race;
        this.mental_ilness = Mental;
        this.body_camera = Body_cam;
    }

    public Entity(){}   

    public String toString() {
       return "Id: " + this.id + "| Name: " + this.name + " |   Gender: " + this.gender + " | date: " + this.date + " | Flee: " + this.flee_status + " | Threat Type: "+ threat_type + 
       " | Warmed With: " + warmed_with + " | City: " + city + " | Country: " + country + " | State" + state + " | Age: " + age + " | Race: " + race + " | Body Camera: " + body_camera + " | Mental Ilness: " + mental_ilness ;
    }

    public byte[] toByteArray(boolean setGrave) throws IOException {
      
        if(setGrave == true){
            dos.writeChar(grave);
        }
        
        dos.writeInt(id);
        dos.writeInt(date);
        dos.writeUTF(threat_type);
        dos.writeUTF(flee_status);
        dos.writeUTF(warmed_with);
        dos.writeUTF(city);
        dos.writeUTF(country);
        dos.writeUTF(state);
        dos.writeUTF(name);
        dos.writeShort(age);
        dos.writeUTF(gender);
        dos.writeUTF(race);
        dos.writeUTF(mental_ilness);
        dos.writeUTF(body_camera);

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
       
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
       
        id = dis.readInt();
        date = dis.readInt();
       
        threat_type = dis.readUTF();
        flee_status = dis.readUTF();
        warmed_with = dis.readUTF();
        city = dis.readUTF();
        country = dis.readUTF();
        state = dis.readUTF();
        name = dis.readUTF();
        age = dis.readShort();
        gender = dis.readUTF();
        race = dis.readUTF();
        mental_ilness = dis.readUTF();
        body_camera = dis.readUTF();
    }
}
