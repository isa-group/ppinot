/*******************************************************************************
 * Signavio Core Components
 * Copyright (C) 2012  Signavio GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.oryxeditor.server.diagram.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.oryxeditor.server.diagram.Point;
import org.oryxeditor.server.diagram.basic.BasicDiagram;
import org.oryxeditor.server.diagram.basic.BasicDiagramBuilder;
import org.oryxeditor.server.diagram.basic.BasicShape;
import org.oryxeditor.server.diagram.label.*;
import org.oryxeditor.server.diagram.label.Anchors.Anchor;
import org.oryxeditor.server.diagram.test.util.FileIO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class BasicDiagramBuilderTest {

	protected static final String DUMMY_JSON = "{ \"resourceId\": \"canvas\" }";
	protected static final String diagramFileNormal = "data/processes/diagram_builder_test.json";
	protected static final String nullAndMissingDiagramFileName = "data/processes/diagram_builder_test_nulls.json";
	protected static final String richtextDiagramFileName = "data/processes/richtext.json";
	protected static String jsonDiagram = FileIO.readWholeFile(diagramFileNormal);
	
	// tolerance for assertEquals
	protected static final Double MAX_DELTA = 0.001;
	
	
	@Test
	public final void testParseStencilsetNamespace() throws JSONException {
		assertNull(BasicDiagramBuilder.parseStencilsetNamespace((JSONObject)null));
		JSONObject tmp = new JSONObject(jsonDiagram);
		assertEquals("http://b3mn.org/stencilset/bpmn2.0#", BasicDiagramBuilder.parseStencilsetNamespace(tmp));
		tmp = new JSONObject(DUMMY_JSON);
		assertNotNull(tmp);
		assertNull(BasicDiagramBuilder.parseStencilsetNamespace(tmp));
	}


	@Test
	public final void testParseJson() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testNullAndMissingShapes() throws JSONException{
		BasicDiagram diagram = BasicDiagramBuilder.parseJson(new JSONObject(FileIO.readWholeFile(nullAndMissingDiagramFileName)));
		
		assertEquals(1, diagram.getChildShapesReadOnly().size());
		BasicShape shape = diagram.getChildShapesReadOnly().get(0);
		assertEquals("sid-E9374170-22CA-4B73-80D4-EB84892C49AA", shape.getResourceId());
		assertEquals(0, shape.getOutgoingsReadOnly().size());
	}
	
	@Test
	public void testLabelPositionings() throws JSONException{
		BasicDiagram diagram = BasicDiagramBuilder.parseJson(new JSONObject(jsonDiagram));
		
		BasicShape test = diagram.getShapeById("sid-E9374170-22CA-4B73-80D4-EB84892C49AA");
		assertNotNull(test);
		List<LabelSettings> labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals(new Point(52d,70d), labels.get(0).getPosition());
		assertEquals(VerticalAlign.MIDDLE, labels.get(0).getAlignVertical());
		assertEquals("text_name", labels.get(0).getReference());
		
		
		test = diagram.getShapeById("sid-C28E70FC-9EA2-4FB0-A760-25FB7D2476F0");
		assertNotNull(test);
		labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals(new Point(36d,15d), labels.get(0).getPosition());
		assertEquals(VerticalAlign.MIDDLE, labels.get(0).getAlignVertical());
		assertEquals(HorizontalAlign.LEFT, labels.get(0).getAlignHorizontal());
		assertEquals(new Anchors(Anchor.RIGHT), labels.get(0).getAnchors());
		assertEquals("text_name", labels.get(0).getReference());
		
		
		test = diagram.getShapeById("sid-9ACB5C79-D970-4320-9FA3-554C81547E62");
		assertNotNull(test);
		labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals("condition", labels.get(0).getReference());
		assertEquals(EdgePosition.END_BOTTOM, labels.get(0).getEdgePos());
		
		
		test = diagram.getShapeById("sid-41506937-B59F-4366-B4F0-C19DA175F30B");
		assertNotNull(test);
		labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals(new Point(970d,840d), labels.get(0).getPosition());
		assertEquals(VerticalAlign.MIDDLE, labels.get(0).getAlignVertical());
		assertEquals(HorizontalAlign.CENTER, labels.get(0).getAlignHorizontal());
		assertEquals("condition", labels.get(0).getReference());
		
		
		test = diagram.getShapeById("sid-211F6A31-AD64-48FE-9785-E8BA1F1CF42B");
		assertNotNull(test);
		labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals(8.045239376229617,  labels.get(0).getDistance(), MAX_DELTA);
		assertEquals(new Point(426.9929361580005,651.4678115787002), labels.get(0).getPosition());
		assertEquals(0, labels.get(0).getFrom().intValue());
		assertEquals(1, labels.get(0).getTo().intValue());
		assertEquals(LabelOrientation.LOWER_RIGHT, labels.get(0).getOrientation());
		assertEquals(VerticalAlign.BOTTOM, labels.get(0).getAlignVertical());
		assertEquals(HorizontalAlign.RIGHT, labels.get(0).getAlignHorizontal());
		assertEquals("condition", labels.get(0).getReference());
		
		
		test = diagram.getShapeById("sid-866AE740-40B3-4829-8993-C46D6EB347B6");
		assertNotNull(test);
		labels = new ArrayList<LabelSettings>(test.getLabelSettings());
		assertEquals(1, labels.size());
		assertEquals(new Point(312d,5d), labels.get(0).getPosition());
		assertEquals(HorizontalAlign.RIGHT, labels.get(0).getAlignHorizontal());
		assertEquals(new Anchors(Anchor.RIGHT), labels.get(0).getAnchors());
		assertEquals("text_name", labels.get(0).getReference());
	}	
	
	@Test
	public void testLabelStyle() throws JSONException{
		BasicDiagram richtextDiagram = BasicDiagramBuilder.parseJson(new JSONObject(FileIO.readWholeFile(richtextDiagramFileName)));
		
		BasicShape shape = richtextDiagram.getShapeById("sid-2FE8AE58-D3AE-42B0-A3DD-584CEE0C290D");
		LabelSettings settings = shape.getLabelSettingsForReference("text_name");
		assertEquals("text_name", settings.getReference());
		assertEquals(8, settings.getStyle().getFontSize().longValue());
		assertTrue(settings.getStyle().isItalic());
		assertFalse(settings.getStyle().isBold());
		assertNull(settings.getStyle().getFontFamily());
		assertNull(settings.getStyle().getFill());
		
		shape = richtextDiagram.getShapeById("sid-C941D7F0-A2C6-43DF-B07E-550B35AFFF22");
		settings = shape.getLabelSettingsForReference("text_name");
		assertEquals("text_name", settings.getReference());
		assertNull(settings.getStyle().getFontSize());
		assertFalse(settings.getStyle().isItalic());
		assertTrue(settings.getStyle().isBold());
		assertNull(settings.getStyle().getFontFamily());
		assertEquals(new Color(0xFF0000), settings.getStyle().getFill());
		
		shape = richtextDiagram.getShapeById("sid-A5CC56DE-00AC-4101-AEFE-8761E58FA8B6");
		settings = shape.getLabelSettingsForReference("text_name");
		assertEquals("text_name", settings.getReference());
		assertEquals(18, settings.getStyle().getFontSize().longValue());
		assertFalse(settings.getStyle().isItalic());
		assertFalse(settings.getStyle().isBold());
		assertNull(settings.getStyle().getFontFamily());
		assertNull(settings.getStyle().getFill());
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testNullObject() throws JSONException{
		BasicDiagramBuilder.parseJson((JSONObject)null);	
	}
}
