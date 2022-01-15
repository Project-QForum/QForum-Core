package cn.jackuxl.qforum.entity;


import cn.jackuxl.qforum.model.Result;

public class Board {
    public int id;
    public String name;
    public int priorityLevel = 10;
    public String description;

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
