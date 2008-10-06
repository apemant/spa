/****license*****************************************************************
**   file: raLLFrames.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.util;

/**
 * Klasa koja handla otvorene ekrane unutar aplikacije
 */

public class raLLFrames {
  static raLLFrames raLLF;
  private java.util.LinkedList rFrMembers = new java.util.LinkedList();
  protected raLLFrames() {
  }

  public static raLLFrames getRaLLFrames() {
    if (raLLF == null) {
      raLLF = new raLLFrames();
    }
    return raLLF;
  }

  public void add(Object frm, startFrame sf) {
    raFrameMember newMember = new raFrameMember(frm,sf);
    if (rFrMembers.contains(newMember)) rFrMembers.remove(newMember);
    rFrMembers.add(newMember);
  }
  public java.util.LinkedList getChildFrames(startFrame sf) {
    java.util.LinkedList retList = new java.util.LinkedList();
    for (int i=0;i<rFrMembers.size();i++) {
      if (rFrMembers.get(i) instanceof raFrameMember) {
        raFrameMember rfm = (raFrameMember)rFrMembers.get(i);
        if (rfm.equalsSF(sf)) retList.add(rfm.frame);
      }
    }
    return retList;
  }
  public startFrame getStartFrame(Object frame) {
    for (int i=0;i<rFrMembers.size();i++) {
      if (rFrMembers.get(i) instanceof raFrameMember) {
        raFrameMember rfm = (raFrameMember)rFrMembers.get(i);
        if (rfm.equalsF(frame)) return rfm.stframe;
      }
    }
    return null;
  }
  public startFrame getMsgStartFrame(Class clasa) {
    startFrame sf = findMsgStartFrame();
    if (sf!=null) return sf;
    if (clasa!=null) sf = findMsgStartFrame(clasa.getPackage());
    if (sf!=null) return sf;
    return (startFrame)getStartFrames().get(0);
  }

  public startFrame getMsgStartFrame() {
    return getMsgStartFrame(null);
  }

  public startFrame findMsgStartFrame(Package pak) {
    System.out.println("pak="+pak);
    java.util.LinkedList ll = getStartFrames();
    for (int i=0;i<ll.size();i++) {
      startFrame SF = (startFrame)ll.get(i);
      if (SF.getClass().getPackage().equals(pak)) {
        return SF;
      }
    }
    return null;

  }
  public startFrame findMsgStartFrame() {
    java.util.LinkedList ll = getStartFrames();
    for (int i=0;i<ll.size();i++) {
      startFrame SF = (startFrame)ll.get(i);
      if ((SF.isVisible())&&(SF.getState()==java.awt.Frame.NORMAL)) return SF;
    }
    return null;
  }
  public java.util.LinkedList getStartFrames() {
    java.util.LinkedList ll = new java.util.LinkedList();
    raFrameMember rfm=null;
    for (int i=0;i<rFrMembers.size();i++) {
      if (rFrMembers.get(i) instanceof raFrameMember) {
        rfm = (raFrameMember)rFrMembers.get(i);
        if (rfm.frame == null) ll.add(rfm.stframe);
      }
    }
    if (ll.size()==0) ll.add(rfm.stframe);
    return ll;
  }
/**
 * TEST
 */
  void printMembers() {
    for (int i=0;i<rFrMembers.size();i++) {
      raFrameMember FM = (raFrameMember)rFrMembers.get(i);
      System.out.print("Member : ");
      System.out.println(i);
      System.out.println(FM.frame);
      System.out.println(FM.stframe);
    }
  }
  void printMembers(java.util.LinkedList ll) {
    for (int i=0;i<ll.size();i++) {
      System.out.print("Member : ");
      System.out.println(i);
      System.out.println(ll.get(i));
    }
  }
//END TEST
  class raFrameMember {
    startFrame stframe;
    Object frame;
    public raFrameMember(Object frameC,startFrame stframeC) {
      frame = frameC;
      stframe = stframeC;
    }
    public boolean equals(Object ob) {
      try {
        raFrameMember rfrm = (raFrameMember)ob;
        if (!rfrm.frame.equals(this.frame)) return false;
        if (!rfrm.stframe.equals(this.stframe)) return false;
        return true;
      } catch (Exception e) {
        return super.equals(ob);
      }
    }

    public boolean equalsSF(startFrame sf) {
      if(this.frame!=null)
        return this.stframe.equals(sf);
      else
        return false;
    }

    public boolean equalsF(Object frm) {
      if(this.frame!=null)
        return this.frame.equals(frm);
      else
        return false;
    }

  }
}