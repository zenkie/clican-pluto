package com.ikidstv.quiz.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;

import com.ikidstv.quiz.bean.ContentTree;
import com.ikidstv.quiz.bean.Identity;
import com.ikidstv.quiz.enumeration.TemplateId;
import com.ikidstv.quiz.model.Image;
import com.ikidstv.quiz.model.LearningPoint;
import com.ikidstv.quiz.model.Matching;
import com.ikidstv.quiz.model.Metadata;
import com.ikidstv.quiz.model.Quiz;
import com.ikidstv.quiz.model.QuizLearningPointRel;
import com.ikidstv.quiz.model.Template;

@Scope(ScopeType.PAGE)
@Name("quizAction")
@Restrict(value = "#{identity.isLoggedIn(true)}")
public class QuizAction extends BaseAction {

	private final static Log log = LogFactory.getLog(QuizAction.class);
	private ContentTree contentTree;
	private ContentTree selectedContentTree;
	private List<Quiz> quizBySelectedContent;

	private Map<String, List<LearningPoint>> learningPointTreeMap;
	private Map<Long, LearningPoint> learningPointIdMap;

	private List<Template> templates;
	private Template selectedTemplate;

	private String learningPoint;
	private Long learningPointId;
	private List<LearningPoint> subLearningPoints;
	private List<LearningPoint> selectedLearningPoints;

	private List<Image> pictures;
	private int pictureIndex;

	private Quiz quiz;
	private Metadata metadata;

	@In
	private Identity identity;

	public void listQuizs() {
		contentTree = this.getContentService().getContentTree();
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), null);
		this.learningPointTreeMap = this.getLearningPointService()
				.getLearningPointWithTreeMap();
		this.learningPointIdMap = new HashMap<Long, LearningPoint>();
		for (List<LearningPoint> ps : learningPointTreeMap.values()) {
			for (LearningPoint lp : ps) {
				this.learningPointIdMap.put(lp.getId(), lp);
			}
		}
		this.templates = this.getTemplateService().getAllTemplates();
	}

	public void selectContent(ContentTree contentTree) {
		this.selectedContentTree = contentTree;
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), selectedContentTree.getContentId());
	}

	public List<Quiz> getQuizBySelectedContent() {
		return quizBySelectedContent;
	}

	public void addQuiz() {
		this.quiz = new Quiz();
		this.quiz.setUser(this.identity.getUser());
		this.quiz.setEpisode(this.selectedContentTree.getName());
		this.quiz.setEpisodeId(this.selectedContentTree.getEpisonId());
		this.quiz.setSeason(this.selectedContentTree.getParent().getName());
		this.quiz.setSeasonId(this.selectedContentTree.getParent()
				.getSeasonId());
		this.learningPoint = learningPointTreeMap.keySet().iterator().next();
		this.subLearningPoints = this.learningPointTreeMap
				.get(this.learningPoint);
		this.selectedLearningPoints = new ArrayList<LearningPoint>();
		this.pictures = this.getImageService().getImageByContent(
				this.selectedContentTree.getSeasonId());
		this.selectedTemplate = null;
	}

	public void saveQuiz() {
		if (this.quiz.getCreateTime() == null) {
			this.quiz.setCreateTime(new Date());
		}
		Set<QuizLearningPointRel> learningPointRelSet = new HashSet<QuizLearningPointRel>();
		for (LearningPoint lp : this.selectedLearningPoints) {
			QuizLearningPointRel rel = new QuizLearningPointRel();
			rel.setLearningPoint(lp);
			rel.setQuiz(this.quiz);
			learningPointRelSet.add(rel);
		}
		this.quiz.setLearningPointRelSet(learningPointRelSet);
		this.quiz.setTemplate(this.selectedTemplate);
		this.getQuizService().saveQuiz(quiz,this.metadata);
		quizBySelectedContent = this.getQuizService().findQuizByUserId(
				identity.getUser().getId(), selectedContentTree.getContentId());
	}
	
	public void previewQuiz(){
		if (this.quiz.getCreateTime() == null) {
			this.quiz.setCreateTime(new Date());
		}
		Set<QuizLearningPointRel> learningPointRelSet = new HashSet<QuizLearningPointRel>();
		for (LearningPoint lp : this.selectedLearningPoints) {
			QuizLearningPointRel rel = new QuizLearningPointRel();
			rel.setLearningPoint(lp);
			rel.setQuiz(this.quiz);
			learningPointRelSet.add(rel);
		}
		this.quiz.setLearningPointRelSet(learningPointRelSet);
		this.quiz.setTemplate(this.selectedTemplate);
	}

	public void changeLearningPoint() {
		this.subLearningPoints = this.learningPointTreeMap
				.get(this.learningPoint);
	}

	public String getTemplateIdName() {
		if(this.selectedTemplate==null){
			return null;
		}
		return this.selectedTemplate.getTemplateId().name();
	}

	public void addLearningPoint() {
		LearningPoint learningPoint = learningPointIdMap
				.get(this.learningPointId);
		if (learningPoint != null) {
			selectedLearningPoints.add(learningPoint);
		}
	}

	public void removeLearningPoint(LearningPoint lp) {
		selectedLearningPoints.remove(lp);
	}

	public void selectTemplate(Template template) {
		this.selectedTemplate = template;
		TemplateId templateId = template.getTemplateId();
		if (templateId == TemplateId.Matching) {
			this.metadata = new Matching();
		}
	}

	public void setPictureIndex(int index){
		this.pictureIndex = index;
	}
	
	public void selectPicture(Image picture) {
		try {
			Method method = metadata.getClass().getMethod("setPicture" + this.pictureIndex,
					String.class);
			method.invoke(metadata, picture.getPath());
		} catch (Exception e) {
			log.error("", e);
		}
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public ContentTree getContentTree() {
		return contentTree;
	}

	public ContentTree getSelectedContentTree() {
		return selectedContentTree;
	}

	public String getLearningPoint() {
		return learningPoint;
	}

	public void setLearningPoint(String learningPoint) {
		this.learningPoint = learningPoint;
	}

	public List<LearningPoint> getSelectedLearningPoints() {
		return selectedLearningPoints;
	}

	public void setSelectedLearningPoints(
			List<LearningPoint> selectedLearningPoints) {
		this.selectedLearningPoints = selectedLearningPoints;
	}

	public List<LearningPoint> getSubLearningPoints() {
		return subLearningPoints;
	}

	public void setSubLearningPoints(List<LearningPoint> subLearningPoints) {
		this.subLearningPoints = subLearningPoints;
	}

	public Long getLearningPointId() {
		return learningPointId;
	}

	public void setLearningPointId(Long learningPointId) {
		this.learningPointId = learningPointId;
	}

	public Map<String, List<LearningPoint>> getLearningPointTreeMap() {
		return learningPointTreeMap;
	}

	public void setLearningPointTreeMap(
			Map<String, List<LearningPoint>> learningPointTreeMap) {
		this.learningPointTreeMap = learningPointTreeMap;
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	public Template getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(Template selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public List<Image> getPictures() {
		return pictures;
	}

	public void setPictures(List<Image> pictures) {
		this.pictures = pictures;
	}

}