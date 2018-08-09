package bbs.com.xinfeng.bbswork.domin;

import java.util.List;

/**
 * Created by dell on 2016/11/23 17:46.
 */
//主题列表
public class ThemeListBean extends ErrorBean {
    public static final String URL = "api/thread/gets";
    /**
     * data : [{"id":597,"content":"很好啊","author_userid":20005,"status":1,"comments":1,"collections":0,"like":1,"displayorder":0,"topic_id":3,"terminal_id":1525745210,"posttableid":7,"last_at":"2018-05-08 11:12:04","last_reply_userid":"20004","created_at":"2018-05-08 10:06:50","updated_at":"2018-05-08 11:26:48","src":["http://provider.proxy.6rooms.net/upload/bbs_thread/20180508/20005/src/20180508100650_leMRYylgDPYgPQvdgEfP.png?width=422&height=750"],"thumb":["http://provider.proxy.6rooms.net/upload/bbs_thread/20180508/20005/300_300/20180508100650_leMRYylgDPYgPQvdgEfP.png"],"hasLike":0,"hasCollect":0,"author_username":"孙悟空","author_block":0,"portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180413/10066/100_100/20180413052242_3yn6rPaUvlfNRLXIWtsS.png","replyUsers":[{"id":20004,"name":"96猫","portrait_thumb":"http://provider.test.6rooms.net/upload/user/20180417/20004/100_100/20180417091227_06GmeBfFQTMQjJIboQsx.png","isblock":1}]}]
     * topic : {"id":3,"name":"人文环境讨论组","introduce":"大家要加群，一起讨论北京的旅游景点，修改一下在哪里呢，？","img_url":"http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png","fans_number":17,"thread_number":341,"create_userid":20002,"status":1,"last_at":"2018-05-15 14:11:06","last_thread_id":1635,"operate_id":0,"created_at":"2018-04-13 12:04:14","updated_at":"2018-05-15 14:11:06","img_url_thumb":"http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png"}
     */

    private TopicBean topic;

    private List<InnerThemeListBean> data;

    public List<InnerThemeListBean> getData() {
        return data;
    }

    public void setData(List<InnerThemeListBean> data) {
        this.data = data;
    }


    public TopicBean getTopic() {
        return topic;
    }

    public void setTopic(TopicBean topic) {
        this.topic = topic;
    }

    public static class TopicBean {
        /**
         * id : 3
         * name : 人文环境讨论组
         * introduce : 大家要加群，一起讨论北京的旅游景点，修改一下在哪里呢，？
         * img_url : http://provider.test.6rooms.net/upload/user/20180413/20002/src/20180413040256_H85UAjWbp5X1MyII6jto.png
         * fans_number : 17
         * thread_number : 341
         * create_userid : 20002
         * status : 1
         * last_at : 2018-05-15 14:11:06
         * last_thread_id : 1635
         * operate_id : 0
         * created_at : 2018-04-13 12:04:14
         * updated_at : 2018-05-15 14:11:06
         * img_url_thumb : http://provider.test.6rooms.net/upload/user/20180413/20002/200_200/20180413040256_H85UAjWbp5X1MyII6jto.png
         */

        private String name;
        private String img_url_thumb;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg_url_thumb() {
            return img_url_thumb;
        }

        public void setImg_url_thumb(String img_url_thumb) {
            this.img_url_thumb = img_url_thumb;
        }
    }


}

