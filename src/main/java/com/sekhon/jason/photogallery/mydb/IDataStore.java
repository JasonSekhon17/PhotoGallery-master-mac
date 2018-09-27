package com.sekhon.jason.photogallery.mydb;

/**
 * Created by jason on 2018-09-20.
 */

public interface IDataStore {
    void saveState(String date);
    String getState();
}
