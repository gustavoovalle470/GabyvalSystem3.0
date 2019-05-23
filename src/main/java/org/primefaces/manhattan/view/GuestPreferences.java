package org.primefaces.manhattan.view;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    public enum LayoutMode {
        SLIM,
        STATIC,
        OVERLAY,
        HORIZONTAL,
        TOGGLE
    };
        
    private String theme = "teal-yellow";

    private LayoutMode layoutMode = LayoutMode.SLIM;

    private boolean lightMenu = true;
                            
	public String getTheme() {		
		return theme;
	}
    
	public void setTheme(String theme) {
		this.theme = theme;
    }
    
    public LayoutMode getLayoutMode() {		
		return layoutMode;
	}
    
	public void setLayoutMode(LayoutMode layoutMode) {
		this.layoutMode = layoutMode;
    }

    public void updateLayoutMode(String mode) {
        this.layoutMode = LayoutMode.valueOf(mode);
    }

    public boolean isLightMenu() {
        return this.lightMenu;
    }

    public void setLightMenu(boolean value) {
        this.lightMenu = value;
    }
    
    public String getLayoutStyleClass() {
        String layoutStyleClass;
        switch(this.layoutMode) {
            case SLIM:
                layoutStyleClass = "layout-slim";
            break;

            case STATIC:
                layoutStyleClass = "layout-static";
            break;

            case OVERLAY:
                layoutStyleClass = "layout-overlay";
            break;

            case HORIZONTAL:
                layoutStyleClass = "layout-horizontal";
            break;

            case TOGGLE:
                layoutStyleClass = "layout-toggle";
            break;

            default:
                layoutStyleClass = "layout-slim";
            break;
        }

        if(this.lightMenu) {
            layoutStyleClass += " layout-light";
        }

        return layoutStyleClass;
    }
}
