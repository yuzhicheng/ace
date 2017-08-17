package com.yzc.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yzc.support.enums.BaseSchoolType;
import com.yzc.support.enums.Gender;
import com.yzc.support.enums.Label;
import com.yzc.support.enums.SchoolEducateFeature;
import com.yzc.vos.CommunityFullInfo;

import java.io.File;
import java.io.FileOutputStream;


/**
 * @author tangcc
 * @date 2017年5月16日 下午5:30:20
 *
 */
public class PDFUtil {

    /** 换行分隔符：\n **/
    private static final String LINE_SEPERATOR = "\n";

    /** 换行分隔符x2：\n\n **/
    private static final String LINE_SEPERATOR_2 = "\n\n";

    /** 1个空格 **/
    private static final String SPACE = " ";

    /** 10个空格 **/
    private static final String SPACE_10 = "          ";

    /** 行高 **/
    private static final Float LINE_HEIGHT = 30F;

    /** 字间距 **/
    private static final Float WORD_SPACING = 2F;

    /**
     * 生成pdf文件
     * 
     * @param outPath 指定全路径文件名（包含pdf格式）
     * @param community 实体信息
     * @return 文件全路径
     * @throws Exception
     * @since
     */
    public static String createPDF(String outPath, CommunityFullInfo community) throws Exception {
        // 设置纸张
        Rectangle rect = new Rectangle(PageSize.A4);
        // 创建文档实例
        Document doc = new Document(rect);
        // 添加中文字体
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

        // 设置字体样式
        Font textFont = new Font(bfChinese, 13, Font.NORMAL); // 正常
        Font boldFont = new Font(bfChinese, 13, Font.BOLD); // 加粗
        Font firstTitleFont = new Font(bfChinese, 22, Font.BOLD); // 一级标题
        Font secondTitleFont = new Font(bfChinese, 15, Font.BOLD); // 二级标题

        // 创建输出流
        PdfWriter.getInstance(doc, new FileOutputStream(new File(outPath)));
        doc.open();
        doc.newPage();

        // 段落
        Paragraph p = new Paragraph();
        // 短语
        Phrase phrase = new Phrase();

        p = new Paragraph("“中国好老师”公益行动计划", firstTitleFont);
        p.setLeading(50);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);

        if (Label.BASE_SCHOOL.getValue().equals(community.getLabel())) {
            p = new Paragraph("基地校登记表" + LINE_SEPERATOR + LINE_SEPERATOR, firstTitleFont);
        }
        else {
            p = new Paragraph("项目校登记表" + LINE_SEPERATOR + LINE_SEPERATOR, firstTitleFont);
        }
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);

        // 创建一个有2列的表格
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[] { 60, 400 }); // 设置列宽
        table.setLockedWidth(true); // 锁定列宽

        PdfPCell cell;
        phrase = new Phrase();
        Chunk chunk = new Chunk("基本情况", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        if (Label.BASE_SCHOOL.getValue().equals(community.getLabel())) {
            cell.setMinimumHeight(370); // 设置单元格高度
        }
        else {
            cell.setMinimumHeight(315); // 设置单元格高度
        }
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        int i = 1;
        phrase = new Phrase(i++ + ". 学校名称：" + (community.getName() == null ? SPACE : community.getName())
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase(i++ + ". 上级教育主管部门：" + (community.getChargeDep() == null ? SPACE : community.getChargeDep())
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        String createTime = "  年  月";
        if (community.getSchoolCreateTime() != null && community.getSchoolCreateTime().length() >= 6) {
            createTime = community.getSchoolCreateTime().substring(0, 4) + " 年 "
                    + community.getSchoolCreateTime().substring(4, 6) + " 月";

        }
        phrase = new Phrase(i++ + ". 建校时间：" + createTime + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        if (Label.BASE_SCHOOL.getValue().equals(community.getLabel())) {
            if (BaseSchoolType.BASE_SCHOOL_AREA.getValue().equals(community.getBaseSchoolType())) {
                phrase = new Phrase(i++ + ". 基地校类别：区县基地校" + LINE_SEPERATOR_2, textFont);
                p.add(phrase);
            }
            else {
                phrase = new Phrase(i++ + ". 基地校类别：地市基地校" + LINE_SEPERATOR_2, textFont);
                p.add(phrase);
            }
            phrase = new Phrase(i++ + ". 基地校证书编号："
                    + (community.getSchoolCertNo() == null ? SPACE : community.getSchoolCertNo()) + LINE_SEPERATOR_2,
                    textFont);
            p.add(phrase);
        }
        phrase = new Phrase(i++ + ". 学校规模：" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         年级  "
                + (community.getSchoolGradeNum() == null ? SPACE : community.getSchoolGradeNum()) + "  个，班级  "
                + (community.getSchoolClassNum() == null ? SPACE : community.getSchoolClassNum()) + "  个"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         学生  "
                + (community.getSchoolStudentNum() == null ? SPACE : community.getSchoolStudentNum()) + "  人，教师  "
                + (community.getSchoolTeacherNum() == null ? SPACE : community.getSchoolTeacherNum()) + "  人"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         青年教师（35岁及以下）  "
                + (community.getYoungTeacherNum() == null ? SPACE : community.getYoungTeacherNum()) + "  人"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         骨干教师：区县级  "
                + (community.getKeyTeacherAreaNum() == null ? SPACE : community.getKeyTeacherAreaNum()) + "  人，地市级  "
                + (community.getKeyTeacherCityNum() == null ? SPACE : community.getKeyTeacherCityNum()) + "  人，省级  "
                + (community.getKeyTeacherProvinceNum() == null ? SPACE : community.getKeyTeacherProvinceNum()) + "  人"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         学科带头人：区县级  "
                + (community.getSubjectLeaderAreaNum() == null ? SPACE : community.getSubjectLeaderAreaNum())
                + "  人，地市级  "
                + (community.getSubjectLeaderCityNum() == null ? SPACE : community.getSubjectLeaderCityNum())
                + "  人，省级  "
                + (community.getSubjectLeaderProvinceNum() == null ? SPACE : community.getSubjectLeaderProvinceNum())
                + "  人" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("         特级教师  "
                + (community.getSpecialTeacherNum() == null ? SPACE : community.getSpecialTeacherNum())
                + "  人，其他荣誉称号的名师  " + (community.getOtherTeacherNum() == null ? SPACE : community.getOtherTeacherNum())
                + "  人"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        String feature = SchoolEducateFeature.getDesc(community.getSchoolEducateFeature());
        if (feature.contains("{desc}")) {
            feature = feature.replaceAll("\\{desc\\}", community.getSchoolEducateFeatureDesc());
        }
        if (feature.length() > 1) {
            feature = feature.substring(0, feature.length() - 1);
        }
        phrase = new Phrase(i++ + ". 育人特色：" + feature, textFont);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk("校长基本情况", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setMinimumHeight(200); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        phrase = new Phrase("姓名： " + (community.getPrincipalName() == null ? SPACE : community.getPrincipalName())
                + SPACE_10 + "性别： " + Gender.getDesc(community.getPrincipalGender()) + SPACE_10 + "年龄："
                + (community.getPrincipalAge() == null ? SPACE : community.getPrincipalAge()) + LINE_SEPERATOR_2,
                textFont);
        p.add(phrase);
        phrase = new Phrase("职称：   " + (community.getPrincipalTitle() == null ? SPACE : community.getPrincipalTitle())
                + SPACE_10 + "手机号码："
                + (community.getPrincipalMobile() == null ? SPACE : community.getPrincipalMobile()) + LINE_SEPERATOR_2,
                textFont);
        p.add(phrase);
        String serviceTime = SPACE;
        if (community.getPrincipalServiceTime() != null && community.getPrincipalServiceTime().length() >= 4) {
            serviceTime = community.getPrincipalServiceTime().substring(0, 4);
        }
        phrase = new Phrase("任现职时间：" + serviceTime + "  年" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("在其他学校任校长的年限： "
                + (community.getPrincipalServiceOthersTime() == null ? SPACE
                        : community.getPrincipalServiceOthersTime()) + "  年" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("所获荣誉：" + (community.getPrincipalHonor() == null ? SPACE : community.getPrincipalHonor()),
                textFont);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setMinimumHeight(200); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk("办学特色", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setMinimumHeight(350); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        phrase = new Phrase();
        chunk = new Chunk(community.getSchoolFeature() == null ? SPACE : community.getSchoolFeature(), textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk("办学成果（近五年所获荣誉）", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setMinimumHeight(400); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        phrase = new Phrase();
        chunk = new Chunk(community.getSchoolAchievements() == null ? SPACE : community.getSchoolAchievements(),
                textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk("共同体互通互助情况", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setMinimumHeight(400); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        phrase = new Phrase("已结成的学校发展共同体情况：" + LINE_SEPERATOR_2, boldFont);
        p.add(phrase);
        phrase = new Phrase("1、手拉手帮扶学校  "
                + (community.getHandInHandSchoolNum() == null ? SPACE : community.getHandInHandSchoolNum())
                + "  所（其中农村学校  "
                + (community.getHandInHandSchoolVillageNum() == null ? SPACE
                        : community.getHandInHandSchoolVillageNum()) + "  所）" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("2、集团化办学  "
                + (community.getCompanySchoolNum() == null ? SPACE : community.getCompanySchoolNum()) + "  所（其中农村学校  "
                + (community.getCompanySchoolVillageNum() == null ? SPACE : community.getCompanySchoolVillageNum())
                + "  所）" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("3、学区制办学  "
                + (community.getDistrictSchoolNum() == null ? SPACE : community.getDistrictSchoolNum())
                + "  所（其中农村学校  "
                + (community.getDistrictSchoolVillageNum() == null ? SPACE : community.getDistrictSchoolVillageNum())
                + "  所）" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("4、其他形式的共同体  "
                + (community.getOtherSchoolNum() == null ? SPACE : community.getOtherSchoolNum()) + "  所（共同体名称："
                + (community.getOtherSchoolName() == null ? SPACE : community.getOtherSchoolName()) + " ）"
                + LINE_SEPERATOR_2 + LINE_SEPERATOR, textFont);
        p.add(phrase);
        phrase = new Phrase("已结成的校长教师发展共同体：" + LINE_SEPERATOR_2, boldFont);
        p.add(phrase);
        phrase = new Phrase("名师工作室： " + (community.getTeacherStudio() == null ? SPACE : community.getTeacherStudio())
                + "  个，其中覆盖学科（"
                + (community.getTeacherStudioDetail() == null ? SPACE : community.getTeacherStudioDetail()) + "）"
                + LINE_SEPERATOR_2 + LINE_SEPERATOR, textFont);
        p.add(phrase);
        phrase = new Phrase("覆盖学校数： " + (community.getCoverSchoolNum() == null ? SPACE : community.getCoverSchoolNum())
                + "  所，参与人数共  "
                + (community.getCoverSchoolPersonNum() == null ? SPACE : community.getCoverSchoolPersonNum()) + "  人"
                + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("名校长工作室： "
                + (community.getPrincipalStudio() == null ? SPACE : community.getPrincipalStudio()) + "  个，带动学校  "
                + (community.getPrincipalStudioSchoolNum() == null ? SPACE : community.getPrincipalStudioSchoolNum())
                + "  个，共  "
                + (community.getPrincipalStudioPersonNum() == null ? SPACE : community.getPrincipalStudioPersonNum())
                + "  人参与。" + LINE_SEPERATOR_2, textFont);
        p.add(phrase);
        phrase = new Phrase("名班主任工作室： " + (community.getMasterStudio() == null ? SPACE : community.getMasterStudio())
                + "  个，带动学校  "
                + (community.getMasterStudioSchoolNum() == null ? SPACE : community.getMasterStudioSchoolNum())
                + "  个，共  "
                + (community.getMasterStudioPersonNum() == null ? SPACE : community.getMasterStudioPersonNum())
                + "  人参与。" + LINE_SEPERATOR_2 + LINE_SEPERATOR, textFont);
        p.add(phrase);
        phrase = new Phrase("其他：" + (community.getOthers() == null ? SPACE : community.getOthers()), textFont);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        phrase = new Phrase();
        chunk = new Chunk("学校需求意见", boldFont);
        chunk.setWordSpacing(WORD_SPACING * 10);
        chunk.setLineHeight(LINE_HEIGHT * 2);
        phrase.add(chunk);
        cell = new PdfPCell(phrase);
        cell.setPaddingLeft(5);
        cell.setPaddingRight(5);
        cell.setMinimumHeight(160); // 设置单元格高度
        cell.setUseAscender(true); // 设置可以居中
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); // 设置水平居中
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); // 设置垂直居中
        table.addCell(cell);

        p = new Paragraph();
        phrase = new Phrase();
        chunk = new Chunk(community.getSchoolSuggestion() == null ? SPACE : community.getSchoolSuggestion(), textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        p.add(phrase);
        cell = new PdfPCell(p);
        cell.setPaddingLeft(10);
        cell.setPaddingRight(10);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setUseAscender(true); // 设置可以居中
        table.addCell(cell);

        doc.add(table);

        p = new Paragraph();
        p.setLeading(20);
        phrase = new Phrase();
        chunk = new Chunk(LINE_SEPERATOR_2 + LINE_SEPERATOR_2 + LINE_SEPERATOR_2 + SPACE_10 + "负责人签字（加盖单位公章）：",
                textFont);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + SPACE_10 + SPACE_10 + "日期：", textFont);
        phrase.add(chunk);
        p.add(phrase);
        doc.add(p);

        doc.newPage();
        p = new Paragraph("“中国好老师”公益行动计划公约" + LINE_SEPERATOR, firstTitleFont);
        p.setLeading(50);
        p.setAlignment(Element.ALIGN_CENTER);
        doc.add(p);

        p = new Paragraph();
        phrase = new Phrase();
        chunk = new Chunk(
                SPACE_10
                        + "中国好老师”公益行动计划面向全国中小学发起，旨在落实和践行习近平总书记“四有”好老师及系列讲话精神，发现和总结基础教育阶段教师和学校的育人经验，提升教师和学校的育人能力，并通过宣传、表彰等行动在全社会营造尊师重教的氛围，推动教育公平，提高教育质量，促进亿万儿童青少年健康成长。"
                        + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "各方均在理念相通、目标一致、坦诚互信、自觉自愿的基础上加入本行动计划。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "本公约旨在确保全体成员能在公益性、互助性原则下实现互利共赢、共同发展的目标，具体条款如下：" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "一、公益行动计划办公室提供以下服务和支持" + LINE_SEPERATOR, boldFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10
                + "1.引导学校和共同体围绕育人主题，整合各方优质教育资源，提升教师的育人能力。有针对性地提供形式多样的专项培训，举办育人论坛、实践工作坊、育人交流营，组织教师到名校参观访问、跟岗学习等活动。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10
                + "2.搭建“中国好老师”公益网络平台，汇集全国各地学校和教师的优秀育人经验、事迹与成果，为学校和共同体在线开展同伴学习、专家交流、资源分享、网络研修、课题协作等活动提供技术支持。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "3.指导组建不同层级、不同形式的教育共同体，形成覆盖全国的共同体网络，与缔约方携手探索共同体常态化运行的有效路径，促进共同体辐射带动的效果最优化、效应最大化。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(
                SPACE_10
                        + "4.引导、帮助学校和共同体总结和提炼一线教师、学校和地区的优秀育人经验，形成易于分享的育人成果形态，以案例集、系列丛书等多种方式出版或推荐到有影响力的报刊杂志上发表；通过举办现场会、巡回演讲、集中研修等方式，将其辐射到更广泛的地区、学校和教师之中。"
                        + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(
                SPACE_10
                        + "5.引导学校和共同体利用公益行动计划官网、微信平台以及当地各种媒介资源展示“好老师”风采，同时联合有影响力的各类媒体，以采访、实录、微电影、画册、会展等多种形式，广泛宣传“好老师”的育人故事、学校的育人成果，传递教育正能量，营造尊师重教的良好氛围，提升教师的职业认同感、自豪感与幸福感。"
                        + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10
                + "6.引导各个学校和共同体有组织、有计划地开展多种形式的评选表彰活动，对公益行动计划中精心组织、有效统筹、积极引领、行动富有创造性、育人成效显著的个人、团体给予表彰奖励。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(
                SPACE_10 + "7.精准把握教育欠发达地区学校的具体需求，推动优质教育资源的有效供给，鼓励通过不同学校共同体之间的跨区域联动，将优质资源辐射至中西部、老少边贫岛等教育发展薄弱地区。"
                        + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "二、公益行动计划缔约方需遵循以下条款" + LINE_SEPERATOR, boldFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "1.贯彻执行党和国家的教育方针、政策，遵守国家法律、法规，遵循社会公德与职业道德。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "2.主动践行“四有”好老师要求，共同维护公益行动计划的公益形象，树立教师群体“学为人师、行为世范”的职业形象。" + LINE_SEPERATOR,
                textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "3.设立行动联络人，建立缔约方与发起方之间的有效沟通渠道，共同探索切实可行的沟通机制，确保信息的高效、准确传递。" + LINE_SEPERATOR,
                textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "4.从教师、学校等个人、群体做起，成为公益行动计划培训、交流、评比、送教等各类活动的发起者、积极响应者、参与者和传播者，成为育人能力提升的积极践行者。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(
                SPACE_10
                        + "5.因地制宜组建适合个体及区域发展的教育共同体，充分发挥优质教育资源的引领示范作用。地市基地校原则上在市属每个区县联动若干所区县基地校，形成“地市-区县”学校共同体；区县基地校再带动若干所乡镇项目校，形成“区县-乡镇”学校共同体，层层互通，形成覆盖全国城乡的学校共同体网络，推动教师、学校、区域之间的互助互学、共建共赢。"
                        + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10
                + "6.通过多种途径，发掘在育人方面有突出贡献、有时代特征的好校长、好干部、好班主任、好学科老师等典型人物及案例，梳理和总结教师和学校的育人经验和成果，并积极促进其传播推广。"
                + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "7.积极表彰和激励育人成效显著的先进个人、群体，多渠道、创造性地传播典型案例，努力营造尊师爱师的环境氛围。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "8.在公益行动计划中发扬主人翁精神，积极建言献策、群策群力，推动公益行动计划的可持续、常态化开展。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "三、共同条款" + LINE_SEPERATOR, boldFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "1.各方须在信守本公约的前提下实施公益行动计划。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "2.各方本着自我约束、共同监督的原则，共同维护本公约。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "3.出现违背公约的行为时，各相关方均应首先选择对话协商解决，同时保留进一步追究的权力。" + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        chunk = new Chunk(SPACE_10 + "4.本公约自签订之日起生效。" + LINE_SEPERATOR + LINE_SEPERATOR, textFont);
        chunk.setWordSpacing(WORD_SPACING);
        chunk.setLineHeight(LINE_HEIGHT);
        phrase.add(chunk);
        p.add(phrase);
        doc.add(p);

        p = new Paragraph(SPACE_10 + "发起方：中国好老师公益行动计划办公室", secondTitleFont);
        doc.add(p);

        p = new Paragraph(SPACE_10 + "缔约方：", secondTitleFont);
        doc.add(p);

        doc.close();
        return outPath;
    }


    /**
     * 
     * @param args
     * @since 
     */
    public static void main(String[] args) {
        try {
            CommunityFullInfo community = new CommunityFullInfo();
            community.setLabel(0);
            community.setSchoolCreateTime("201306");
            community.setPrincipalServiceTime("2011");
            community.setPrincipalServiceOthersTime(2);
            community.setSchoolEducateFeature(33);
            community.setSchoolEducateFeatureDesc("test描述");
            community.setPrincipalHonor("校长所获荣誉");
            PDFUtil.createPDF("F://yzc.pdf", community);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
