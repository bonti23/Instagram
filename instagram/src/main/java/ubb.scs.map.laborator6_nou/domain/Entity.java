package ubb.scs.map.laborator_6nou.domain;

public class Entity<ID>{
    private ID identityKey;

    public ID getID(){
        return identityKey;
    }

    public void setID(ID identityKey){
        this.identityKey = identityKey;
    }
}
