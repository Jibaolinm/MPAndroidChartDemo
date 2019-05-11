package com.petterp.guosai.GuosaiTest.DingzhiBanChe;

import java.util.List;

/**
 * @author Petterp on 2019/5/10
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class WeizhangBean {

    /**
     * ERRMSG : 成功
     * ROWS_DETAIL : [{"id":1,"name":"1路","map":"/images/bus/bus_001.png","mileage":"20","ticket":8,"sites":["巨龙大道","宋家岗","航空总部","天河机场","建设大道双墩","建设大道新合村","解放大道水厂","解放大道太平洋","解放大道仁寿路","解放大道宝丰路","解放大道同济医院","解放大道航空路","解放大道中山公园","友谊路中山大道口","中山大道地铁六渡桥站","中山大道地铁江汉路站","保华街黄石路","中山大道大智路","胜利街一元路","胜利街三阳路","胜利街六合路","胜利街张自忠路","芦沟桥路","汉口火车站","后襄河北路"],"time":[{"site":"巨龙大道","starttime":"06:23:00","endtime":"21:19:00"},{"site":"后襄河北路","starttime":"06:00:00","endtime":"23:12:00"}]},{"id":2,"name":"2路","map":"/images/bus/bus_002.png","mileage":"10","ticket":3,"sites":["光谷广场","杨家湾","虎泉","广埠屯","街道口","宝通寺","中南路","洪山广场","小龟山","螃蟹岬","积玉桥","江汉路","循礼门","中山公园","青年路","王家墩东","范湖","汉口火车站","长港路","常青花园","金银潭","常青城","宏图大道","盘龙城"],"time":[{"site":"光谷广场","starttime":"05:42:00","endtime":"23:19:00"},{"site":"盘龙城","starttime":"05:47:00","endtime":"23:27:00"}]},{"id":3,"name":"3路","map":"/images/bus/bus_003.png","mileage":"12","ticket":5,"sites":["青年路市博物馆","青年路范湖","青年路机场河","青年路雪松路","青年路地铁青年路站","武胜路武胜西街","鹦鹉大道古琴台","长江大桥汉阳桥头","黄鹤楼南路","首义路阅马场","张之洞路千家街","武昌火车站综合体","珞雄路公交场站","珞雄路光谷步行街","珞喻路湖北省中医院","珞喻路关山口","珞喻东路叶麻店","珞喻东路大黄村"],"time":[{"site":"青年路市博物馆","starttime":"06:23:00","endtime":"23:27:00"},{"site":"珞喻东路大黄村","starttime":"06:23:00","endtime":"23:30:00"}]},{"id":4,"name":"14路","map":"/images/bus/bus_004.png","mileage":"22","ticket":6,"sites":["珞喻东路佳园路","珞喻东路森林公园","珞喻东路油篓口","珞喻东路长山","珞喻东路高坡店","森林大道青王路","森林大道石门峰公园","森林大道王家店","森林大道宝盖峰","森林大道九峰","森林大道土桥","森林大道土吴路","土吴路花山","大长山路土吴路","大长山路城铁花山南站","大长山路清正路","花山大道大长山路","花山大道春和路","花城大道花山大道","花城大道软件新城","花城大道春和路","春和路花城大道口"],"time":[{"site":"珞喻东路佳园路","starttime":"06:23:00","endtime":"23:27:00"},{"site":"春和路花城大道口","starttime":"06:30:00","endtime":"23:40:00"}]},{"id":5,"name":"15路","map":"/images/bus/bus_005.png","mileage":"14","ticket":7,"sites":["春和路小雷村","碧桂园公交场站","建设十路和平大道口","建设十路红钢三街","建设十路临江大道口","临江大道建设十路","临江大道5.5互联网产业园","临江大道建设八路","临江大道建设七路","临江大道建设六路","临江大道建设五路","临江大道建设四路","抚顺街建设四路","抚顺街建设三路","抚顺街建设二路","抚顺街建设一路"],"time":[{"site":"春和路小雷村","starttime":"06:23:00","endtime":"23:27:00"},{"site":"抚顺街建设一路","starttime":"06:23:00","endtime":"23:45:00"}]},{"id":6,"name":"16路","map":"/images/bus/bus_006.png","mileage":"23","ticket":9,"sites":["抚顺街三弓路","园林路余家头路","园林路和平大道口","和平大道奥山世纪城","和平大道杨家路","和平大道余家头","和平大道杨园","和平大道四美塘"],"time":[{"site":"抚顺街三弓路","starttime":"06:40:00","endtime":"23:27:00"},{"site":"和平大道四美塘","starttime":"06:23:00","endtime":"23:10:00"}]},{"id":7,"name":"17路","map":"/images/bus/bus_007.png","mileage":"15","ticket":5,"sites":["和平大道徐家棚","和平大道惠誉花园","和平大道武车路","和平大道三角路","和平大道三层楼","临江大道新河街","临江大道曾家巷","临江大道四马路","中山路大堤口","解放路司门口"],"time":[{"site":"和平大道徐家棚","starttime":"06:23:00","endtime":"23:27:00"},{"site":"解放路司门口","starttime":"06:45:00","endtime":"23:27:00"}]},{"id":8,"name":"28路","map":"/images/bus/bus_008.png","mileage":"12","ticket":4,"sites":["临江大道汉阳门","分金街韦桑路口","分金街永泰路口","黄浦大街分金街","中山大道长江二桥","芦沟桥路地铁黄浦路站","解放大道永清街","解放大道劳动街","解放大道球场街","台北路解放大道口","台北路台北一路","台北路鲩子湖","台北路鄂城墩","台北路天门墩","常青五路杨汊湖小区","常青五路姑嫂树路口","姑嫂树路杨汊湖"],"time":[{"site":"临江大道汉阳门","starttime":"06:23:00","endtime":"23:27:00"},{"site":"姑嫂树路杨汊湖","starttime":"06:23:00","endtime":"23:10:00"}]},{"id":9,"name":"29路","map":"/images/bus/bus_009.png","mileage":"24","ticket":9,"sites":["姑嫂树路市中心医院后湖院区","姑嫂树路石桥","唐家墩路地铁唐家墩站","唐家墩路唐蔡路","唐家墩路万达广场","新华路马场角","新华路北湖","新华路取水楼","新华路协和医院","京汉大道前进一路","前进四路民主一街","民生路花楼街"],"time":[{"site":"姑嫂树路市中心医院后湖院区","starttime":"06:55:00","endtime":"23:27:00"},{"site":"民生路花楼街","starttime":"06:23:00","endtime":"23:27:00"}]},{"id":10,"name":"10路","map":"/images/bus/bus_010.png","mileage":"22","ticket":7,"sites":["民生路黄陂街","沿江大道武汉港","沿江大道兰陵路","沿江大道粤汉码头","胜利街六合路","胜利街张自忠路","中山大道长江二桥","徐东大街地铁徐东站","徐东大街地铁汪家墩站","徐东大街沙湖路","徐东大街地铁梨园站","二环线梨园","梨园医院","东湖隧道银杏原路","红庙公交场站"],"time":[{"site":"民生路黄陂街","starttime":"06:23:00","endtime":"23:27:00"},{"site":"红庙公交场站","starttime":"06:23:00","endtime":"23:27:00"}]}]
     * RESULT : S
     */

    private String ERRMSG;
    private String RESULT;
    private List<ROWSDETAILBean> ROWS_DETAIL;

    public String getERRMSG() {
        return ERRMSG;
    }

    public void setERRMSG(String ERRMSG) {
        this.ERRMSG = ERRMSG;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<ROWSDETAILBean> getROWS_DETAIL() {
        return ROWS_DETAIL;
    }

    public void setROWS_DETAIL(List<ROWSDETAILBean> ROWS_DETAIL) {
        this.ROWS_DETAIL = ROWS_DETAIL;
    }

    public static class ROWSDETAILBean {
        /**
         * id : 1
         * name : 1路
         * map : /images/bus/bus_001.png
         * mileage : 20
         * ticket : 8
         * sites : ["巨龙大道","宋家岗","航空总部","天河机场","建设大道双墩","建设大道新合村","解放大道水厂","解放大道太平洋","解放大道仁寿路","解放大道宝丰路","解放大道同济医院","解放大道航空路","解放大道中山公园","友谊路中山大道口","中山大道地铁六渡桥站","中山大道地铁江汉路站","保华街黄石路","中山大道大智路","胜利街一元路","胜利街三阳路","胜利街六合路","胜利街张自忠路","芦沟桥路","汉口火车站","后襄河北路"]
         * time : [{"site":"巨龙大道","starttime":"06:23:00","endtime":"21:19:00"},{"site":"后襄河北路","starttime":"06:00:00","endtime":"23:12:00"}]
         */

        private int id;
        private String name;
        private String map;
        private String mileage;
        private int ticket;
        private List<String> sites;
        private List<TimeBean> time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public String getMileage() {
            return mileage;
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public int getTicket() {
            return ticket;
        }

        public void setTicket(int ticket) {
            this.ticket = ticket;
        }

        public List<String> getSites() {
            return sites;
        }

        public void setSites(List<String> sites) {
            this.sites = sites;
        }

        public List<TimeBean> getTime() {
            return time;
        }

        public void setTime(List<TimeBean> time) {
            this.time = time;
        }

        public static class TimeBean {
            /**
             * site : 巨龙大道
             * starttime : 06:23:00
             * endtime : 21:19:00
             */

            private String site;
            private String starttime;
            private String endtime;

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }

            public String getStarttime() {
                return starttime;
            }

            public void setStarttime(String starttime) {
                this.starttime = starttime;
            }

            public String getEndtime() {
                return endtime;
            }

            public void setEndtime(String endtime) {
                this.endtime = endtime;
            }
        }
    }
}
