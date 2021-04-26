package com.example.myapplication3;



public class Model {

    String task,description,id,date,quantity,mtime,mdate,name,mpriority;

    public void setMpriority(String mpriority) {
        this.mpriority = mpriority;
    }

    public String getMpriority() {
        return mpriority;
    }

    public Model(String task, String id){
        this.task=task;
        this.id=id;

    }
    public Model(String task, String description, String id, String date)
    {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;

    }
    public Model(String task, String description, String id, String date,String quantity)
    {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.quantity = quantity;
    }
    public Model(String task, String description, String id, String date,String mdate,String mtime,String mpriority)
    {
        this.task = task;
        this.description = description;
        this.id = id;
        this.date = date;
        this.mdate=mdate;
        this.mtime=mtime;
        this.mpriority=mpriority;
    }
    public Model(){

    }
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getmdate() {
        return mdate;
    }

    public void setmdate(String mdate) {
        this.mdate = mdate;
    }

    public String getmtime() {
        return mtime;
    }

    public void setmtime(String mtime) {
        this.mtime = mtime;
    }


    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

   public String getName() {
       return name;
    }

    public void setName(String name) {
      this.name = name;
    }
}
