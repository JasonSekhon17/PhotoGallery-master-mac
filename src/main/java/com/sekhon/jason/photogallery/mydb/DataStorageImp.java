package com.sekhon.jason.photogallery.mydb;

/**
 * Created by jason on 2018-09-20.
 */

public class DataStorageImp implements IDataStore{
    public String state_ = null;
    public void saveState(String state){state_ = state;}
    public String getState(){return state_;}
}
