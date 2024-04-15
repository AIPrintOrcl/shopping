package com.example.user.shopping;

public class Management { // 해당 사용자에게 물품이 중복되면 처리하는 클래스

    int foodID;

    public boolean validate(int managementID) { // 테이블 management 안에 사용자에게 등록되어 있는 foodID
        if (managementID != foodID)
        {
            return true;
        }
        else if(managementID == foodID)
        {
            return false;
        }
        return true;
    }

}
