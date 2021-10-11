package com.biaozhunyuan.tianyi.login;

/**
 * Created by lenovo on 2018/4/10.
 */

public class Tag {
    private int Corp;

    private Entity Entity;

    private int 分类;


    public void setCorp(int Corp) {
        this.Corp = Corp;
    }

    public int getCorp() {
        return this.Corp;
    }

    public void setEntity(Entity Entity) {
        this.Entity = Entity;
    }

    public int get分类() {
        return 分类;
    }

    public void set分类(int 分类) {
        this.分类 = 分类;
    }

    public Entity getEntity() {
        return this.Entity;
    }



    public static class Entity {

        /**
         * 21 新建客户
         * 22 行业类型
         * 23 主营产品
         */
        private int 分类;

        private String 名称;

        public void set分类(int 分类) {
            this.分类 = 分类;
        }

        public int get分类() {
            return this.分类;
        }

        public void set名称(String 名称) {
            this.名称 = 名称;
        }

        public String get名称() {
            return this.名称;
        }

        @Override
        public String toString() {
            return "Entity{" +
                    "分类=" + 分类 +
                    ", 名称='" + 名称 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Tag{" +
                "Corp='" + Corp + '\'' +
                ", Entity=" + Entity +
                '}';
    }
}
