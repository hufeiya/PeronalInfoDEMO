package com.hufeiya.personinfocollecter.mailPaserTest;

import com.hufeiya.personinfocollecter.utils.mail.NetEaseMailParser;

import org.junit.Test;

/**
 * Created by hufeiya on 16-7-29.
 */
public class NetEaseMailPaserTest {
    private static String json = "{\n" +
            "'code':'S_OK',\n" +
            "'var':[{\n" +
            "'id':'91:1tbiWxSzalSIQmNqMAAAsx',\n" +
            "'fid':1,\n" +
            "'size':18738,\n" +
            "'from':'\"云课堂\" <no-reply@study.163.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'职场新人如何赢得老板青睐？',\n" +
            "'sentDate':new Date(2016,6,27,17,33,8),\n" +
            "'receivedDate':new Date(2016,6,27,17,33,8),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'read':true,\n" +
            "'report':true},\n" +
            "'expirytime':new Date(2016,7,26),\n" +
            "'hmid':'<318633430.8477781.1469611988247.JavaMail.study@study62.server.163.org>'},\n" +
            "{\n" +
            "'id':'21:1tbiFQ2yalWBW9UQpgAAss',\n" +
            "'fid':1,\n" +
            "'size':18005,\n" +
            "'from':'\"京东JD.com\" <customer_service@jd.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'您在京东还有待评价的商品哦，发表商品评价即可得京豆！立即评价>',\n" +
            "'sentDate':new Date(2016,6,26,9,17,33),\n" +
            "'receivedDate':new Date(2016,6,26,9,17,33),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'7090_360buycom'},\n" +
            "'hmid':'<880001623.3227421469495853203.JavaMail.admin@host-10-187-18-100>'},\n" +
            "{\n" +
            "'id':'235:1tbi6x4HGlXlUu2i6QAAbs',\n" +
            "'fid':1,\n" +
            "'size':9475,\n" +
            "'from':'\"网易严选\" <yanxuan1@service.netease.com>',\n" +
            "'to':'\"网易邮箱用户\" <user@netease.com>',\n" +
            "'subject':'【高温补贴】你有3款内野制造商洗浴用品可选择免费领取（逾期失效）！',\n" +
            "'sentDate':new Date(2016,6,26,6,12,12),\n" +
            "'receivedDate':new Date(2016,6,26,6,13,6),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'autodel':true},\n" +
            "'expirytime':new Date(2016,9,18,6,12,14)},\n" +
            "{\n" +
            "'id':'271:xtbBDxCvalO-1qckkgAAsx',\n" +
            "'fid':1,\n" +
            "'size':19018,\n" +
            "'from':'\"Facebook\" <notification+kjdmdhv7h-wm@facebookmail.com>',\n" +
            "'to':'\"Feiya Hu\" <394764416@163.com>',\n" +
            "'subject':'Feiya  Hu，你有2 条新通知和2 条消息',\n" +
            "'sentDate':new Date(2016,6,23,15,32,34),\n" +
            "'receivedDate':new Date(2016,6,23,15,33,4),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'read':true},\n" +
            "'hmid':'<e103bfae011c086085cefbf3e09a8623@async.facebook.com>'},\n" +
            "{\n" +
            "'id':'15:1tbiDxOtalUL8u+2SQAAsn',\n" +
            "'fid':1,\n" +
            "'size':18644,\n" +
            "'from':'\"云课堂\" <no-reply@study.163.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'提高效率的5个方法，你会用几个？',\n" +
            "'sentDate':new Date(2016,6,21,12,52,3),\n" +
            "'receivedDate':new Date(2016,6,21,12,52,3),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'report':true,\n" +
            "'autodel':true},\n" +
            "'expirytime':new Date(2016,7,20),\n" +
            "'hmid':'<836437831.5347603.1469076723084.JavaMail.study@study63.server.163.org>'},\n" +
            "{\n" +
            "'id':'62:1tbiPh6malXlhU5chwAAsM',\n" +
            "'fid':1,\n" +
            "'size':18739,\n" +
            "'from':'\"云课堂\" <no-reply@study.163.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'奇葩说冠军邱晨在线义诊，治疗设计丑病！',\n" +
            "'sentDate':new Date(2016,6,14,14,49,33),\n" +
            "'receivedDate':new Date(2016,6,14,14,49,34),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'read':true,\n" +
            "'report':true},\n" +
            "'expirytime':new Date(2016,7,13),\n" +
            "'hmid':'<1752701318.814411.1468478973937.JavaMail.study@study62.server.163.org>'},\n" +
            "{\n" +
            "'id':'91:1tbiWwqkalSIQX6tjAAAsd',\n" +
            "'fid':1,\n" +
            "'size':18951,\n" +
            "'from':'\"Facebook\" <notification+kjdmdhv7h-wm@facebookmail.com>',\n" +
            "'to':'\"Feiya Hu\" <394764416@163.com>',\n" +
            "'subject':'Feiya  Hu，你有2 条新通知和2 条消息',\n" +
            "'sentDate':new Date(2016,6,12,15,30,9),\n" +
            "'receivedDate':new Date(2016,6,12,15,30,50),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<00d6720b43858d5a90697dd359e281e0@async.facebook.com>'},\n" +
            "{\n" +
            "'id':'465:xtbB0QGfalUL-d420QAAs-',\n" +
            "'fid':1,\n" +
            "'size':18606,\n" +
            "'from':'\"云课堂\" <no-reply@study.163.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'7月最值得推荐的10门课 ',\n" +
            "'sentDate':new Date(2016,6,7,18,35,12),\n" +
            "'receivedDate':new Date(2016,6,7,18,35,13),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "'report':true,\n" +
            "'autodel':true},\n" +
            "'expirytime':new Date(2016,7,6),\n" +
            "'hmid':'<531058410.2489691.1467887712856.JavaMail.study@study63.server.163.org>'},\n" +
            "{\n" +
            "'id':'15:1tbiDx+WalUL8X4c1wAAs2',\n" +
            "'fid':1,\n" +
            "'size':4057,\n" +
            "'from':'\"Amazon Web Services, LLC\" <no-reply-aws@amazon.com>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'Your AWS Account is about to be suspended',\n" +
            "'sentDate':new Date(2016,5,29,3,12,59),\n" +
            "'receivedDate':new Date(2016,5,29,3,13,3),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<01000155986dc7fd-b4a58316-a2c4-4f3c-8154-148b591475c3-000000@email.amazonses.com>'},\n" +
            "{\n" +
            "'id':'91:1tbiWwqValSIQHfO8AABs6',\n" +
            "'fid':1,\n" +
            "'size':12681,\n" +
            "'from':'\"12306@rails.com.cn\" <12306@rails.com.cn>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'网上购票系统-用户支付通知',\n" +
            "'sentDate':new Date(2016,5,27,22,45,7),\n" +
            "'receivedDate':new Date(2016,5,27,22,45,7),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'5310_12306'},\n" +
            "'hmid':'<1384190670.276467008.1467038707920.JavaMail.jboss@10.1.214.135>'},\n" +
            "{\n" +
            "'id':'91:1tbiWwqValSIQHfO8AAAs7',\n" +
            "'fid':1,\n" +
            "'size':24451,\n" +
            "'from':'\"京东JD.com\" <customer_service@jd.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'您有1张京东优惠券即将到期，快去及时使用，享受优惠吧',\n" +
            "'sentDate':new Date(2016,5,27,12,57,46),\n" +
            "'receivedDate':new Date(2016,5,27,12,57,46),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'7090_360buycom'},\n" +
            "'hmid':'<1843269570.36803481467003466239.JavaMail.admin@host-10-187-58-144>'},\n" +
            "{\n" +
            "'id':'50:1tbiMh+QalWBSPzgtwAAsg',\n" +
            "'fid':1,\n" +
            "'size':4699,\n" +
            "'from':'\"Amazon Web Services\" <no-reply-aws@amazon.com>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'Your AWS Account has been suspended for non-payment',\n" +
            "'sentDate':new Date(2016,5,23,3,29,21),\n" +
            "'receivedDate':new Date(2016,5,23,3,30,39),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<0100015579969ad5-66e21883-aff9-4880-ba7f-b69855bf7484-000000@email.amazonses.com>'},\n" +
            "{\n" +
            "'id':'235:1tbi6wuPalXlUMLFeQAAsb',\n" +
            "'fid':1,\n" +
            "'size':4054,\n" +
            "'from':'\"Amazon Web Services, LLC\" <no-reply-aws@amazon.com>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'Your AWS Account is about to be suspended',\n" +
            "'sentDate':new Date(2016,5,22,3,12,37),\n" +
            "'receivedDate':new Date(2016,5,22,3,12,43),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<010001557460ec64-2a65d950-aafe-4632-8ace-ac05f2a071ab-000000@email.amazonses.com>'},\n" +
            "{\n" +
            "'id':'91:1tbiWwyMalSIP+LYFQAAs9',\n" +
            "'fid':1,\n" +
            "'size':19116,\n" +
            "'from':'\"Facebook\" <notification+kjdmdhv7h-wm@facebookmail.com>',\n" +
            "'to':'\"Feiya Hu\" <394764416@163.com>',\n" +
            "'subject':'Feiya  Hu，你有1 条新通知和2 条消息',\n" +
            "'sentDate':new Date(2016,5,18,14,40,42),\n" +
            "'receivedDate':new Date(2016,5,18,14,40,44),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<b2c6eaf4395fe5f57d2b15be87447465@async.facebook.com>'},\n" +
            "{\n" +
            "'id':'235:1tbi6xGLalXlUHU5LgAAsZ',\n" +
            "'fid':1,\n" +
            "'size':24857,\n" +
            "'from':'\"京东JD.com\" <customer_service@jd.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'您有1张京东优惠券即将到期，快去及时使用，享受优惠吧',\n" +
            "'sentDate':new Date(2016,5,17,12,40,17),\n" +
            "'receivedDate':new Date(2016,5,17,12,40,17),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'7090_360buycom'},\n" +
            "'hmid':'<1501414148.28750891466138417203.JavaMail.admin@host-10-187-57-21>'},\n" +
            "{\n" +
            "'id':'465:xtbB0QmKalUL-GfHzgAAs0',\n" +
            "'fid':1,\n" +
            "'size':24783,\n" +
            "'from':'\"京东JD.com\" <customer_service@jd.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'您有1张京东优惠券即将到期，快去及时使用，享受优惠吧',\n" +
            "'sentDate':new Date(2016,5,16,11,50,1),\n" +
            "'receivedDate':new Date(2016,5,16,11,50,1),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'7090_360buycom'},\n" +
            "'hmid':'<729359023.119231466049001116.JavaMail.admin@host-10-187-57-221>'},\n" +
            "{\n" +
            "'id':'21:1tbiFQmJalWBWSuEBQABsZ',\n" +
            "'fid':1,\n" +
            "'size':24750,\n" +
            "'from':'\"京东JD.com\" <customer_service@jd.com>',\n" +
            "'to':'394764416@163.com',\n" +
            "'subject':'您在京东有1张优惠券到账，请注意查收',\n" +
            "'sentDate':new Date(2016,5,15,13,6,16),\n" +
            "'receivedDate':new Date(2016,5,15,13,6,18),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'unscaned',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'7090_360buycom'},\n" +
            "'hmid':'<1683269327.23881071465967176894.JavaMail.admin@host-10-187-18-98>'},\n" +
            "{\n" +
            "'id':'62:1tbiPgaHalXlgzoJ4gAAs3',\n" +
            "'fid':1,\n" +
            "'size':3929,\n" +
            "'from':'\"Amazon Web Services, LLC\" <no-reply-aws@amazon.com>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'Problems with your AWS Account - Please read urgently',\n" +
            "'sentDate':new Date(2016,5,14,3,11,54),\n" +
            "'receivedDate':new Date(2016,5,14,3,12,6),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<010001554b2d67f5-35d08e40-5bea-4202-8ac8-3c0cafda73c3-000000@email.amazonses.com>'},\n" +
            "{\n" +
            "'id':'271:xtbBDw2DalO-08WdbgAAsi',\n" +
            "'fid':1,\n" +
            "'size':3931,\n" +
            "'from':'\"Amazon Web Services, LLC\" <no-reply-aws@amazon.com>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'Problems with your AWS Account - Please read urgently',\n" +
            "'sentDate':new Date(2016,5,10,3,11,31),\n" +
            "'receivedDate':new Date(2016,5,10,3,11,41),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'hmid':'<0100015536939d60-4c404056-c899-40b9-a15f-2865675521a9-000000@email.amazonses.com>'},\n" +
            "{\n" +
            "'id':'15:1tbiDxuCalUL8B5JcQAAs0',\n" +
            "'fid':1,\n" +
            "'size':12688,\n" +
            "'from':'\"12306@rails.com.cn\" <12306@rails.com.cn>',\n" +
            "'to':'\"394764416@163.com\" <394764416@163.com>',\n" +
            "'subject':'网上购票系统-用户支付通知',\n" +
            "'sentDate':new Date(2016,5,9,0,39,24),\n" +
            "'receivedDate':new Date(2016,5,9,0,39,23),\n" +
            "'priority':3,\n" +
            "'backgroundColor':0,\n" +
            "'antiVirusStatus':'novirus',\n" +
            "'label0':0,\n" +
            "'flags':{\n" +
            "},\n" +
            "'ctrls':{\n" +
            "'RulesType':'5310_12306'},\n" +
            "'hmid':'<1451184611.154112010.1465403964370.JavaMail.jboss@10.1.214.135>'}],\n" +
            "'midoffset':-1}";
    @Test
    public void testNetEaseMailPaser() throws Throwable{
        NetEaseMailParser parser = new NetEaseMailParser();
        parser.parseDateToTimeStamp(json);
    }
}
