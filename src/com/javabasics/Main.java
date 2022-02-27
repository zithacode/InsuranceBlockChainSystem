package com.javabasics;

import netscape.javascript.JSObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.security.MessageDigest;

class Client{

   private String firstName;
   private String lastName;
   private String email;
   private boolean isPaid;

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isPaid = false;

    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Client{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isPaid=" + isPaid +
                '}';
    }
}

class InsurancePay{

    private  Client client;
    private int amount;
    private Date date;

    public InsurancePay(Client client, int amount, Date date) {
        this.client = client;
        this.amount = amount;
        this.date = date;
        this.payStatus();
    }

    void payStatus(){
        this.client.setPaid(true);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

class Block {

    private long timestamp;
    private String prevHash;
    private String hash;
    private InsurancePay insurancePay;

    public Block(long timestamp, InsurancePay insurancePay ) {
        this.timestamp = timestamp;
        this.prevHash = "000000";
        this.insurancePay = insurancePay;
        this.hash = this.calculateHash();
    }

    public String calculateHash(){
        return applySha256(this.prevHash +
                Long.toString(this.timestamp) + this.insurancePay);
    }

    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Getters and Setters
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public InsurancePay getInsurancePay() {
        return insurancePay;
    }

    public void setInsurancePay(InsurancePay insurancePay) {
        this.insurancePay = insurancePay;
    }
}

class BlockChain{

    private List<Block> chain = new ArrayList<>();

    public BlockChain() {

        this.chain.add(this.genesisBlock());
    }


    public Block genesisBlock(){
        return new Block(new Date().getTime(), null);
    }

    public void addBlock(Block newBlock){
        int chainSize = this.chain.size();
        newBlock.setPrevHash(this.chain.get(chainSize-1).getHash());
        newBlock.setHash(newBlock.calculateHash());
        this.chain.add(newBlock);
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
    }

    public boolean isChainValid(){
        for(int i = 1; i < this.chain.size(); i++){

            Block currentBlock = this.chain.get(i);
            Block prevBlock = this.chain.get(this.chain.size()-1);

            if(currentBlock.getHash() != currentBlock.calculateHash()){
                return false;
            }
            if (currentBlock.getPrevHash() != prevBlock.getHash()){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlockChain{" +
                "chain=" + chain +
                '}';
    }
}


public class Main {

    public static void main(String[] args) {
	// write your code here

        Client client1 = new Client("Sipho", "Zitha", "msipho351@gmailcom");
        Client client2 = new Client("Zingah", "Khoza", "zingkhoza@gmail.com");
        Client client3 = new Client("Jayz", "Ntini", "ntinis@gmail.com");
        Client client4 = new Client("west", "kanye", "west.kanye@gmail.com");

        InsurancePay insurancePay1 = new InsurancePay(client1, 500, new Date());
        InsurancePay insurancePay2 = new InsurancePay(client2, 500, new Date());
        InsurancePay insurancePay3 = new InsurancePay(client3, 500, new Date());
        InsurancePay insurancePay4 = new InsurancePay(client4, 500, new Date());


        //creating array of blocks
        List<Block> blocks = new ArrayList<>();
        blocks.add(new Block(new Date().getTime(), insurancePay1));
        blocks.add(new Block(new Date().getTime(), insurancePay2));
        blocks.add(new Block(new Date().getTime(), insurancePay3));
        blocks.add(new Block(new Date().getTime(), insurancePay4));



        BlockChain blockChain =  new BlockChain();
        //adding blocks in the chain
        for( Block block : blocks ){
            blockChain.addBlock(block);
        }

        int count = 1;
        for (Block block : blockChain.getChain() ){
            System.out.println(
                    "--------------------------------------------------------------------------\n"+
                            "Block " + count +"\n"+
                    "Timestamp " + block.getTimestamp() +"\n"
                    + "Data "+ block.getInsurancePay()+ "\n"
                    + "PreviousHash "+ block.getPrevHash()+ "\n"
                    + "Hash " + block.getHash()
            );
            count++;
        }

    }
}
