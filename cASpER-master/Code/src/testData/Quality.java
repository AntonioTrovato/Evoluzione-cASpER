public class Quality {

    private Item[] items;

    public Quality(Item[] items){
        this.items=items;
    }

    public void UpdateQuality(){
        for (int i=0;i<items.length;i++)
        {
            if((!items[i].getName().equals("AgedBried")) && (!items[i].getName().equals("Backstage passes to a TAFKAL80ETC concert")))
            {
                if (items[i].getQuality()>0)
                {
                    if(!items[i].equals("Sulfuras, Hand of Ragnaros"))
                    {
                        items[i].setQuality(items[i].getQuality()-1);
                    }
                }
            }
            else
            {
                if (items[i].getQuality() < 50)
                {
                    items[i].setQuality(items[i].getQuality()+1);
                    if (items[i].getName().equals("Backstage passes to a TAFKAL80ETC concert"))
                    {
                        if(items[i].getSellIn()<11)
                        {
                            if(items[i].getQuality()<50)
                            {
                                items[i].setQuality(items[i].getQuality()+1);
                            }
                        }
                    }
                }
            }
            if(!items[i].getName().equals("Sulfuras, Hand of Ragnaros"))
            {
                items[i].setSellIn(items[i].getSellIn() - 1);
            }
            if(items[i].getSellIn()<0)
            {
              if(!items[i].getName().equals("Aged Brie"))
              {
                  if(!items[i].getName().equals("Backstage passes to a TAFKAL80ETC concert"))
                  {
                      if(items[i].getQuality()>0)
                      {
                          if (!items[i].getName().equals("Sulfuras, Hand of Ragnaros"))
                          {
                              items[i].setQuality(items[i].getQuality()-1);
                          }
                      }
                  }
              }
              else
              {
                  items[i].setQuality(0);
              }

            }
            else
            {
                if (items[i].getQuality()< 50)
                {
                    items[i].setQuality(items[i].getQuality()+1);
                }
            }
        }
    }

}
