package com.nerin.nims.opt.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Zach on 16/5/22.
 */
public class NerinBase {
    protected Logger getLogger(){
        return LoggerFactory.getLogger(this.getClass());
    }
}
