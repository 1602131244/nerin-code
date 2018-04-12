package com.nerin.nims.opt;

import com.nerin.nims.opt.app.domain.EbsDuty;
import com.nerin.nims.opt.app.domain.EbsDutyCode;
import com.nerin.nims.opt.app.service.EbsDutyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NimsOptApplication.class)
@WebAppConfiguration
@ActiveProfiles("dev")
public class NimsOptApplicationTests {

	@Autowired
	private EbsDutyService ebsDutyService;

	@Test
	public void contextLoads() {
//		EbsDuty ebsDuty = new EbsDuty();
//		ebsDuty.setNerinIdentity("A");
//		ebsDuty.setCodeRemark("阿萨德");
//		List<EbsDutyCode> arr = new ArrayList<EbsDutyCode>();
//		EbsDutyCode c = new EbsDutyCode();
//		c.setDutyCode("123");
//		arr.add(c);
//		c = new EbsDutyCode();
//		c.setDutyCode("456");
//		arr.add(c);
//		ebsDuty.setEbsCodes(arr);
//		ebsDutyService.insertDuty(ebsDuty);
	}

//	@Test
//	public void up() {
//		EbsDuty ebsDuty = ebsDutyService.getEbsDutyById(2382l);
//		ebsDuty.setCodeRemark("阿萨德");
//		List<EbsDutyCode> arr = ebsDuty.getEbsCodes();
//		arr.remove(0);
//		ebsDutyService.insertDuty(ebsDuty);
//	}

}
