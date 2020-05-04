package itesm.juradohja.nfcprototype;

import java.io.Serializable;

public class TagProfile implements Serializable {

    public String id;
    public String name;
    public String birthDate;
    public String bloodType;
    public String weight;
    public String height;
    public String hospital;
    public String ailments;
    public String allergies;
    public String contactName;
    public String contactPhone;
    public String contactRelationship;

    public TagProfile() {
        this.id = "";
        this.name = "";
        this.birthDate = "";
        this.bloodType = "";
        this.weight = "";
        this.height = "";
        this.hospital = "";
        this.ailments = "";
        this.allergies = "";
        this.contactName = "";
        this.contactPhone = "";
        this.contactRelationship = "";
    }

    public TagProfile(String[] records) {
        this.id = records[0];
        this.name = records[1];
        this.birthDate = records[2];
        this.bloodType = records[3];
        this.weight = records[4];
        this.height = records[5];
        this.hospital = records[6];
        this.ailments = records[7];
        this.allergies = records[8];
        this.contactName = records[9];
        this.contactPhone = records[10];
        this.contactRelationship = records[11];
    }

    public TagProfile(String name, String birthDate, String bloodType, String weight, String height, String hospital, String ailments, String allergies, String contactName, String contactPhone, String contactRelationship) {
        this.id = "00001";
        this.name = name;
        this.birthDate = birthDate;
        this.bloodType = bloodType;
        this.weight = weight;
        this.height = height;
        this.hospital = hospital;
        this.ailments = ailments;
        this.allergies = allergies;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactRelationship = contactRelationship;
    }
}
