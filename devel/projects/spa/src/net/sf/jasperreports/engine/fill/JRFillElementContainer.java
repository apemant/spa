/*jadclipse*/// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) nonlb space radix(10) lradix(10) 
// Source File Name:   JRFillElementContainer.java

package net.sf.jasperreports.engine.fill;

import java.util.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.util.JRStyleResolver;

// Referenced classes of package net.sf.jasperreports.engine.fill:
//            JRFillElementGroup, JRFillElement, JRFillFrame, JRYComparator, 
//            JRFillBreak, JRFillSubreport, JRFillCrosstab, JRFillObjectFactory, 
//            JRBaseFiller, JRFillExpressionEvaluator, JRFillCloneFactory, JROriginProvider

public abstract class JRFillElementContainer extends JRFillElementGroup {

    protected JRFillElementContainer(JRBaseFiller filler, JRElementGroup container, JRFillObjectFactory factory) {
        super(container, factory);
        ySortedElements = null;
        stretchElements = null;
        bandBottomElements = null;
        removableElements = null;
        willOverflow = false;
        isOverflow = false;
        stretchHeight = 0;
        firstY = 0;
        firstYElement = null;
        stylesToEvaluate = new HashSet();
        evaluatedStyles = new HashMap();
        expressionEvaluator = factory.getExpressionEvaluator();
        initDeepElements();
        this.filler = filler;
    }

    protected JRFillElementContainer(JRFillElementContainer container, JRFillCloneFactory factory) {
        super(container, factory);
        ySortedElements = null;
        stretchElements = null;
        bandBottomElements = null;
        removableElements = null;
        willOverflow = false;
        isOverflow = false;
        stretchHeight = 0;
        firstY = 0;
        firstYElement = null;
        stylesToEvaluate = new HashSet();
        evaluatedStyles = new HashMap();
        expressionEvaluator = container.expressionEvaluator;
        initDeepElements();
        filler = container.filler;
    }

    private void initDeepElements() {
        if (elements == null) {
            deepElements = new JRFillElement[0];
        } else {
            List deepElementsList = new ArrayList(elements.length);
            collectDeepElements(elements, deepElementsList);
            deepElements = new JRFillElement[deepElementsList.size()];
            deepElementsList.toArray(deepElements);
        }
    }

    private static void collectDeepElements(JRElement elements[], List deepElementsList) {
        for (int i = 0; i < elements.length; i++) {
            JRElement element = elements[i];
            deepElementsList.add(element);
            if (element instanceof JRFillFrame) {
                JRFrame frame = (JRFrame)element;
                collectDeepElements(frame.getElements(), deepElementsList);
            }
        }

    }

    protected final void initElements() {
        hasPrintWhenOverflowElement = false;
        if (elements != null && elements.length > 0) {
            List sortedElemsList = new ArrayList();
            List stretchElemsList = new ArrayList();
            List bandBottomElemsList = new ArrayList();
            List removableElemsList = new ArrayList();
            for (int i = 0; i < elements.length; i++) {
                JRFillElement element = elements[i];
                sortedElemsList.add(element);
                if (element.getPositionType() == 3)
                    bandBottomElemsList.add(element);
                if (element.getStretchType() != 0)
                    stretchElemsList.add(element);
                if (element.isRemoveLineWhenBlank())
                    removableElemsList.add(element);
                if (element.isPrintWhenDetailOverflows())
                    hasPrintWhenOverflowElement = true;
            }

            Collections.sort(sortedElemsList, new JRYComparator());
            ySortedElements = new JRFillElement[elements.length];
            sortedElemsList.toArray(ySortedElements);
            stretchElements = new JRFillElement[stretchElemsList.size()];
            stretchElemsList.toArray(stretchElements);
            bandBottomElements = new JRFillElement[bandBottomElemsList.size()];
            bandBottomElemsList.toArray(bandBottomElements);
            removableElements = new JRFillElement[removableElemsList.size()];
            removableElemsList.toArray(removableElements);
        }
        setDependentElements();
    }

    private void setDependentElements() {
        if (ySortedElements != null && ySortedElements.length > 0) {
            for (int i = 0; i < ySortedElements.length - 1; i++) {
                JRFillElement iElem = ySortedElements[i];
                boolean isBreakElem = iElem instanceof JRFillBreak;
                for (int j = i + 1; j < ySortedElements.length; j++) {
                    JRFillElement jElem = ySortedElements[j];
                    int left = Math.min(iElem.getX(), jElem.getX());
                    int right = Math.max(iElem.getX() + iElem.getWidth(), jElem.getX() + jElem.getWidth());
                    if ((isBreakElem && jElem.getPositionType() == 2 || jElem.getPositionType() == 1) && iElem.getY() + iElem.getHeight() <= jElem.getY() && iElem.getWidth() + jElem.getWidth() > right - left)
                        iElem.addDependantElement(jElem);
                }

            }

        }
    }

    protected void evaluate(byte evaluation) throws JRException {
        JRElement allElements[] = getElements();
        if (allElements != null && allElements.length > 0) {
            for (int i = 0; i < allElements.length; i++) {
                JRFillElement element = (JRFillElement)allElements[i];
                element.setCurrentEvaluation(evaluation);
                element.evaluate(evaluation);
            }

        }
    }

    protected void resetElements() {
        if (ySortedElements != null && ySortedElements.length > 0) {
            for (int i = 0; i < ySortedElements.length; i++) {
                JRFillElement element = ySortedElements[i];
                element.reset();
                if (!isOverflow)
                    element.setAlreadyPrinted(false);
            }

        }
    }

    public boolean willOverflow() {
        return willOverflow;
    }

    protected void initFill() {
        isOverflow = willOverflow;
        firstY = 0;
        firstYElement = null;
    }

    protected void prepareElements(int availableHeight, boolean isOverflowAllowed) throws JRException {
        boolean tmpWillOverflow = false;
        int maxBandStretch = 0;
        int bandStretch = 0;
        firstY = isOverflow ? getContainerHeight() : 0;
        firstYElement = null;
        boolean isFirstYFound = false;
        if (ySortedElements != null && ySortedElements.length > 0) {
            for (int i = 0; i < ySortedElements.length; i++) {
                JRFillElement element = ySortedElements[i];
                tmpWillOverflow = element.prepare(availableHeight + getElementFirstY(element), isOverflow) || tmpWillOverflow;
                element.moveDependantElements();
                if (!element.isToPrint())
                    continue;
                if (isOverflow) {
                    if (element.isReprinted())
                        firstY = 0;
                    else
                    if (!isFirstYFound)
                        firstY = element.getY();
                    isFirstYFound = true;
                }
                firstYElement = element;
                /*bandStretch = (element.getRelativeY() + element.getStretchHeight()) - element.getY() - element.getHeight();*/
                bandStretch = (element.getRelativeY() + element.getStretchHeight()) - getContainerHeight();
                if (bandStretch > maxBandStretch)
                    maxBandStretch = bandStretch;
                
            }

        }
        if (maxBandStretch > (availableHeight - getContainerHeight()) + firstY)
            tmpWillOverflow = true;
        if (tmpWillOverflow)
            stretchHeight = availableHeight;
        else
            stretchHeight = getContainerHeight() + maxBandStretch;
        willOverflow = tmpWillOverflow && isOverflowAllowed;
    }

    private int getElementFirstY(JRFillElement element) {
        int elemFirstY;
        if (!isOverflow || hasPrintWhenOverflowElement)
            elemFirstY = 0;
        else
        if (element.getY() >= firstY)
            elemFirstY = firstY;
        else
            elemFirstY = element.getY();
        return elemFirstY;
    }

    protected void setStretchHeight(int stretchHeight) {
        if (stretchHeight > this.stretchHeight)
            this.stretchHeight = stretchHeight;
    }

    protected void stretchElements() {
        if (stretchElements != null && stretchElements.length > 0) {
            for (int i = 0; i < stretchElements.length; i++) {
                JRFillElement element = stretchElements[i];
                element.stretchElement(stretchHeight - getContainerHeight());
                element.moveDependantElements();
            }

        }
        if (ySortedElements != null && ySortedElements.length > 0) {
            for (int i = 0; i < ySortedElements.length; i++) {
                JRFillElement element = ySortedElements[i];
                element.stretchHeightFinal();
            }

        }
    }

    protected int getStretchHeight() {
        return stretchHeight;
    }

    protected void moveBandBottomElements() {
        if (bandBottomElements != null && bandBottomElements.length > 0) {
            for (int i = 0; i < bandBottomElements.length; i++) {
                JRFillElement element = bandBottomElements[i];
                element.setRelativeY((element.getY() + stretchHeight) - getContainerHeight());
                element.setToPrint(element.isToPrint() && !willOverflow);
            }

        }
    }

    protected void removeBlankElements() {
        JRElement remElems[] = removableElements;
        if (remElems != null && remElems.length > 0) {
            JRElement elems[] = ySortedElements;
            for (int i = 0; i < remElems.length; i++) {
                JRFillElement iElem = (JRFillElement)remElems[i];
                int blankHeight;
                if (iElem.isToPrint())
                    blankHeight = iElem.getHeight() - iElem.getStretchHeight();
                else
                    blankHeight = iElem.getHeight();
                if (blankHeight <= 0 || iElem.getRelativeY() + iElem.getStretchHeight() > stretchHeight || iElem.getRelativeY() < firstY)
                    continue;
                int blankY = (iElem.getRelativeY() + iElem.getHeight()) - blankHeight;
                boolean isToRemove = true;
                for (int j = 0; j < elems.length; j++) {
                    JRFillElement jElem = (JRFillElement)elems[j];
                    if (iElem == jElem || !jElem.isToPrint())
                        continue;
                    int top = Math.min(blankY, jElem.getRelativeY());
                    int bottom = Math.max(blankY + blankHeight, jElem.getRelativeY() + jElem.getStretchHeight());
                    if (blankHeight + jElem.getStretchHeight() <= bottom - top)
                        continue;
                    isToRemove = false;
                    break;
                }

                if (!isToRemove)
                    continue;
                for (int j = 0; j < elems.length; j++) {
                    JRFillElement jElem = (JRFillElement)elems[j];
                    if (jElem.getRelativeY() >= blankY + blankHeight)
                        jElem.setRelativeY(jElem.getRelativeY() - blankHeight);
                }

                stretchHeight = stretchHeight - blankHeight;
            }

        }
    }

    public void fillElements(JRPrintElementContainer printContainer) throws JRException {
        JRElement allElements[] = getElements();
        if (allElements != null && allElements.length > 0) {
            for (int i = 0; i < allElements.length; i++) {
                JRFillElement element = (JRFillElement)allElements[i];
                element.setRelativeY(element.getRelativeY() - firstY);
                if (element.getRelativeY() + element.getStretchHeight() > stretchHeight)
                    element.setToPrint(false);
                element.setAlreadyPrinted(element.isToPrint() || element.isAlreadyPrinted());
                if (!element.isToPrint())
                    continue;
                JRPrintElement printElement = element.fill();
                if (printElement == null)
                    continue;
                printContainer.addElement(printElement);
                if (element instanceof JRFillSubreport) {
                    JRFillSubreport subreport = (JRFillSubreport)element;
                    List fonts = subreport.subreportFiller.getJasperPrint().getFontsList();
                    for (int j = 0; j < fonts.size(); j++)
                        filler.getJasperPrint().addFont((JRReportFont)fonts.get(j), true);

                    List styles = subreport.subreportFiller.getJasperPrint().getStylesList();
                    for (int j = 0; j < styles.size(); j++)
                        filler.addPrintStyle((JRStyle)styles.get(j));

                    List origins = subreport.subreportFiller.getJasperPrint().getOriginsList();
                    for (int j = 0; j < origins.size(); j++)
                        filler.getJasperPrint().addOrigin((JROrigin)origins.get(j));

                    Collection printElements = subreport.getPrintElements();
                    addSubElements(printContainer, element, printElements);
                    continue;
                }
                if (element instanceof JRFillCrosstab) {
                    List printElements = ((JRFillCrosstab)element).getPrintElements();
                    addSubElements(printContainer, element, printElements);
                }
            }

        }
        printContainer.setHeight(stretchHeight - firstY);
    }

    protected void addSubElements(JRPrintElementContainer printContainer, JRFillElement element, Collection printElements) {
        if (printElements != null && printElements.size() > 0) {
            JRPrintElement printElement;
            for (Iterator it = printElements.iterator(); it.hasNext(); printContainer.addElement(printElement)) {
                printElement = (JRPrintElement)it.next();
                printElement.setX(element.getX() + printElement.getX());
                printElement.setY(element.getRelativeY() + printElement.getY());
            }

        }
    }

    protected void rewind() throws JRException {
        if (ySortedElements != null && ySortedElements.length > 0) {
            for (int i = 0; i < ySortedElements.length; i++) {
                JRFillElement element = ySortedElements[i];
                element.rewind();
                element.setAlreadyPrinted(false);
            }

        }
        willOverflow = false;
    }

    protected int getFirstY() {
        return firstY;
    }

    protected abstract int getContainerHeight();

    protected void initConditionalStyles() {
        filler.addDefaultStyleListener(new JRBaseFiller.DefaultStyleListener() {
            public void defaultStyleSet(JRStyle style) {
                collectConditionalStyle(style);
            }
        });
        for (int i = 0; i < deepElements.length; i++) {
            JRStyle style = deepElements[i].initStyle;
            collectConditionalStyle(style);
        }

        if (deepElements.length > 0) {
            for (int i = 0; i < deepElements.length; i++)
                deepElements[i].setConditionalStylesContainer(this);

        }
    }

    protected void collectConditionalStyle(JRStyle style) {
        if (style != null)
            stylesToEvaluate.add(style);
    }

    protected void evaluateConditionalStyles(byte evaluation) throws JRException {
        for (Iterator it = stylesToEvaluate.iterator(); it.hasNext(); evaluateConditionalStyle((JRStyle)it.next(), evaluation));
    }

    protected JRStyle evaluateConditionalStyle(JRStyle initialStyle, byte evaluation) throws JRException {
        JRStyle consolidatedStyle = initialStyle;
        StringBuffer code = new StringBuffer();
        List condStylesToApply = new ArrayList();
        boolean anyTrue = buildConsolidatedStyle(initialStyle, evaluation, code, condStylesToApply);
        if (anyTrue) {
            String consolidatedStyleName = initialStyle.getName() + code.toString();
            consolidatedStyle = (JRStyle)filler.getJasperPrint().getStylesMap().get(consolidatedStyleName);
            if (consolidatedStyle == null) {
                consolidatedStyle = new JRBaseStyle(consolidatedStyleName);
                for (int j = condStylesToApply.size() - 1; j >= 0; j--)
                    JRStyleResolver.appendStyle(consolidatedStyle, (JRStyle)condStylesToApply.get(j));

                filler.addPrintStyle(consolidatedStyle);
            }
        }
        evaluatedStyles.put(initialStyle, consolidatedStyle);
        return consolidatedStyle;
    }

    protected boolean buildConsolidatedStyle(JRStyle style, byte evaluation, StringBuffer code, List condStylesToApply) throws JRException {
        boolean anyTrue = false;
        JRConditionalStyle conditionalStyles[] = style.getConditionalStyles();
        if (conditionalStyles != null && conditionalStyles.length > 0) {
            for (int j = 0; j < conditionalStyles.length; j++) {
                JRConditionalStyle conditionalStyle = conditionalStyles[j];
                Boolean expressionValue = (Boolean)expressionEvaluator.evaluate(conditionalStyle.getConditionExpression(), evaluation);
                boolean condition;
                if (expressionValue == null)
                    condition = false;
                else
                    condition = expressionValue.booleanValue();
                code.append(condition ? '1' : '0');
                anyTrue |= condition;
                if (condition)
                    condStylesToApply.add(conditionalStyle);
            }

        }
        condStylesToApply.add(style);
        if (style.getStyle() != null)
            anyTrue |= buildConsolidatedStyle(style.getStyle(), evaluation, code, condStylesToApply);
        return anyTrue;
    }

    public JRStyle getEvaluatedConditionalStyle(JRStyle parentStyle) {
        return (JRStyle)evaluatedStyles.get(parentStyle);
    }

    protected final void setElementOriginProvider(JROriginProvider originProvider) {
        if (originProvider != null) {
            for (int i = 0; i < deepElements.length; i++)
                deepElements[i].setOriginProvider(originProvider);

        }
    }

    protected JRBaseFiller filler;
    private JRFillElement ySortedElements[];
    private JRFillElement stretchElements[];
    private JRFillElement bandBottomElements[];
    private JRFillElement removableElements[];
    private boolean willOverflow;
    protected boolean isOverflow;
    private int stretchHeight;
    private int firstY;
    protected JRFillElement firstYElement;
    protected final JRFillExpressionEvaluator expressionEvaluator;
    protected JRFillElement deepElements[];
    protected Set stylesToEvaluate;
    protected Map evaluatedStyles;
    protected boolean hasPrintWhenOverflowElement;
}


/*
    DECOMPILATION REPORT

    Decompiled from: /home/ante/projekti/devel/projects/spa/thirdparty-jars/xjasperreports-3.5.2.jar
    Total time: 7 ms
    Jad reported messages/errors:
The class file version is 48.0 (only 45.3, 46.0 and 47.0 are supported)
The class file version is 48.0 (only 45.3, 46.0 and 47.0 are supported)
    Exit status: 0
    Caught exceptions:
*/