package com.biaozhunyuan.tianyi.chatLibary.chat.model;


import java.util.ArrayList;

/**
 * Created by：Administrator on 2015/12/21 16:43
 */
public class TestData {

    public static ArrayList<ItemModel> getTestAdData() {
        ArrayList<ItemModel> models = new ArrayList<>();
        ChatModel model = new ChatModel();
        model.setContent("你好？");
        model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
        models.add(new ItemModel(ItemModel.CHAT_Left, model));
        ChatModel model2 = new ChatModel();
        model2.setContent("你好！你是？");
        model2.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
        models.add(new ItemModel(ItemModel.CHAT_RIGHT, model2));
        ChatModel model3 = new ChatModel();
        model3.setContent("你猜？");
        model3.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
        models.add(new ItemModel(ItemModel.CHAT_Left, model3));
        ChatModel model4 = new ChatModel();
        model4.setContent("猜不出来，你说吧");
        model4.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
        models.add(new ItemModel(ItemModel.CHAT_RIGHT, model4));
        ChatModel model5 = new ChatModel();
        model5.setContent("我是隔壁老王啊");
        model5.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
        models.add(new ItemModel(ItemModel.CHAT_Left, model5));
        ChatModel model6 = new ChatModel();
        model6.setContent("哦，老王啊，你好你好");
        model6.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
        models.add(new ItemModel(ItemModel.CHAT_RIGHT, model6));
        return models;
    }

}
