package com.king.hhczy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.hhczy.entity.BichromaticSphere;
import com.king.hhczy.mapper.BichromaticSphereMapper;
import com.king.hhczy.service.IBichromaticSphereService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 双色球开奖结果 服务实现类
 * </p>
 *
 * @author ningjinxiang
 * @since 2020-09-15
 */
@Service
public class BichromaticSphereServiceImpl extends ServiceImpl<BichromaticSphereMapper, BichromaticSphere> implements IBichromaticSphereService {

    @Override
    public String analyse(String[] redNos,int blueNo) {
        //中奖情况
        int one = 0, two = 0, three = 0, four = 0, five = 0, six = 0;
        //累计奖金
        long prize = 0;
        //补足2位，个位数前面补0
        Set<String> inRedNos = new HashSet(Arrays.stream(redNos).map(x -> String.format("%02d", Integer.parseInt(x.trim()))).collect(Collectors.toList()));
        String blueNoStr = String.format("%02d", blueNo);

        List<BichromaticSphere> allData = this.list();
        for (BichromaticSphere allDatum : allData) {
            List<String> resultRedNos = new ArrayList();
            resultRedNos.add(allDatum.getOne());
            resultRedNos.add(allDatum.getTwo());
            resultRedNos.add(allDatum.getThree());
            resultRedNos.add(allDatum.getFour());
            resultRedNos.add(allDatum.getFive());
            resultRedNos.add(allDatum.getSix());
            long redRight = resultRedNos.stream().filter(x -> inRedNos.contains(x)).count();
            //蓝区中否？
            if (blueNoStr.equals(allDatum.getSeven())) {
                if (redRight==0||redRight==1||redRight==2) {//六等奖
                    six++;
                    prize += 5;
                }else if(redRight==3){//五
                    five++;
                    prize += 10;
                }else if(redRight==4){//四
                    four++;
                    prize += 200;
                }else if(redRight==5){//三
                    three++;
                    prize += 3000;
                }else if(redRight==6){//一
                    one++;
                    prize += Long.parseLong(allDatum.getFirstPrizeBonus().replace(",","").replace("，",""));
                }
            }else {
                if (redRight==4) {//五等奖
                    five++;
                    prize += 10;
                }else if(redRight==5){//四
                    four++;
                    prize += 200;
                }else if(redRight==6){//二
                    two++;
                    prize += Long.parseLong(allDatum.getSecondPrizeBonus().replace(",","").replace("，",""));
                }
            }
        }
        String reduce = inRedNos.stream().reduce("", (x, y) -> x + " " + y);
        String analyseSb = String.format("%s-%s<br/>若每天买一注，历史累计中奖情况：<br/> 一等奖%s注<br/> 二等奖%s注<br/> 三等奖%s注<br/> 四等奖%s注<br/> 五等奖%s注<br/> 六等奖%s注<br/> 累计奖金%s元<br/> 成本%s元",
                reduce,blueNoStr,one,two,three,four,five,six,prize,allData.size()*2);
        return analyseSb;
    }
}
