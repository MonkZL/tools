package com.doctoryun.common;


import com.doctoryun.bean.HospitalInfo;

import java.util.List;

/**
 * @Description 调用系统拍照或进入图库中选择照片,再进行裁剪,压缩.
 * @author 疯尘丶
 */
public class PopupUtil {

    public static int getCurSexSel(String sex){
        if(sex=="0")
            return 0;
        else
           return 1;
    }
    public static int getCurHospitalSel(String hospital_id, List<HospitalInfo> list){
        if(hospital_id == null || hospital_id == "" || list == null)
            return 0;

        int i = 0;
        int cur_sel = Integer.parseInt(hospital_id);

        for(i=0;i<list.size();i++){
            int hospital_sort = Integer.parseInt(list.get(i).getId());
            if(cur_sel == hospital_sort)
                return i;
        }

        return 0;
    }

    public static String getTechTitle(String titleId){
         String[] titleItems = new String[]{"无", "助理医师", "医师", "主治医师", "副主任医师", "主任医师"};//0,1,2,3,4,5
         String ret_str;
         if(titleId != null && titleId.isEmpty() == false){
            int id = Integer.parseInt(titleId);
             ret_str = titleItems[id];
         }else{
             ret_str = titleItems[0];
         }
        return ret_str;
    }
    public static String getMedGrpRole(String roleId){
        String[] roleItems = new String[]{"组长", "副组长", "组员"};
        String ret_str;
        if(roleId != null && roleId.isEmpty() == false){
            int id = Integer.parseInt(roleId);
            ret_str = roleItems[id - 1];
        }else{
            ret_str = roleItems[0];
        }
        return ret_str;
    }

}