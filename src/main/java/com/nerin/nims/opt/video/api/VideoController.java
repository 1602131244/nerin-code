package com.nerin.nims.opt.video.api;


import com.nerin.nims.opt.video.dto.*;
import com.nerin.nims.opt.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    /**
     * 收藏夹
     */
    @RequestMapping(value = "myFavorite", method = RequestMethod.GET)
    public List<MyFavoriteDTO> myFavorite(@RequestParam(value = "personID", required = false, defaultValue = "") Long personId) {
        return videoService.myFavorite(personId);
    }

    /**
     * 收藏夹
     */
    @RequestMapping(value = "myRecord", method = RequestMethod.GET)
    public List<MyRecordDTO> myRecord(@RequestParam(value = "personID", required = false, defaultValue = "") Long personId) {
        return videoService.myRecord(personId);
    }

    /**
     * 根据结构取下层节点
     */
    @RequestMapping(value = "allVideo", method = RequestMethod.GET)
    public PageDTO allVideo(@RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                            @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize,
                            @RequestParam(value = "ID", required = false, defaultValue = "") Long id,
                            @RequestParam(value = "perId", required = false, defaultValue = "") long perId) {
        return videoService.getAllVideo(id, pageNo, pageSize,perId);
    }

    /**
     * 查找视频
     */
    @RequestMapping(value = "searchVideo", method = RequestMethod.GET)
    public PageDTO searchVideo(@RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                               @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize,
                               @RequestParam(value = "queryTerm", required = false, defaultValue = "") String queryTerm,
                               @RequestParam(value = "perId", required = false, defaultValue = "") long perId) {
        return videoService.searchVideo(queryTerm, pageNo, pageSize, perId);
    }


    /**
     * 取结构
     */
    @RequestMapping(value = "getNode", method = RequestMethod.GET)
    public List<NodeDTO> getNode(@RequestParam(value = "ID", required = false, defaultValue = "") Long id) {
        return videoService.getNode(id);
    }

    /**
     * 取最后位置
     */
    @RequestMapping(value = "getLastPosition", method = RequestMethod.GET)
    public List<RecordDTO> getLastPosition(@RequestParam(value = "ID", required = false, defaultValue = "") Long id,
                                           @RequestParam(value = "personID", required = false, defaultValue = "") Long personId) {
        return videoService.getLastPosition(id, personId);
    }

    /**
     * 执行保存记录
     */
    @RequestMapping(value = "saveRecord", method = RequestMethod.POST)
    public Map saveRecord(@RequestBody List<SaveRecordDTO> saveRecordDTO) {
        return videoService.saveRecord(saveRecordDTO);
    }

    /**
     * 执行保存收藏
     */
    @RequestMapping(value = "saveFavorite", method = RequestMethod.POST)
    public Map saveFavorite(@RequestBody List<FavoriteDTO> favoriteDTOs) {
        return videoService.saveFavorite(favoriteDTOs);
    }

    /**
     * 执行保存评论
     */
    @RequestMapping(value = "saveComment", method = RequestMethod.POST)
    public Map saveComment(@RequestBody List<CommentDTO> commentDTOs) {
        return videoService.saveComment(commentDTOs);
    }

    /**
     * 执行更新评论
     */
    @RequestMapping(value = "updateComment", method = RequestMethod.POST)
    public Map updateComment(@RequestBody List<CommentDTO> commentDTOs) {
        return videoService.updateComment(commentDTOs);
    }

    /**
     * 执行保存评论
     */
    @RequestMapping(value = "saveGrade", method = RequestMethod.POST)
    public Map saveGrade(@RequestBody List<GradeDTO> gradeDTOs) {
        return videoService.saveGrade(gradeDTOs);
    }

    /**
     * 执行更新评论
     */
    @RequestMapping(value = "updateGrade", method = RequestMethod.POST)
    public Map updateGrade(@RequestBody List<GradeDTO> gradeDTOs) {
        return videoService.updateGrade(gradeDTOs);
    }

    /**
     * 删除
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Map deleteAll(@RequestBody DeleteDTO deleteDTO) {
        return videoService.deleteAll(deleteDTO.getRows(), deleteDTO.getType());
    }
}


