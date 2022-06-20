public class QualityMakeRefactor {

    private Item[] items;

    public QualityMakeRefactor(Item[] items){
        this.items=items;
    }

    public void DecrementQualityForNormalItems(int i){
        if (items[i].getQuality() > 0)
        {
            if (!items[i].getName().equals("Sulfuras, Hand of Ragnaros"))
            {
                items[i].setQuality(items[i].getQuality()-1);
            }
        }
    }


    public void IncrementQuality(int i){
        if (items[i].getQuality() < 50)
        {
            items[i].setQuality(items[i].getQuality()+1);
        }
    }

    public void UpdateQualityForExpiredItems(int i){
        if (items[i].getQuality()<50)
        {
            items[i].setQuality(items[i].getQuality()+1);
        }
        if (items[i].getSellIn() < 11) {
            IncrementQuality(i);
        }
        if (items[i].getSellIn()<6){
            IncrementQuality(i);
        }
    }

    public void UpdateQualityForItemsThatAgeWell(int i){
        if(!items[i].getName().equals("Aged Brie"))
        {
            if (!items[i].getName().equals("Backstage passes to a TAFKAL80ETC concert"))
            {
                DecrementQualityForNormalItems(i);
            }
            else
            {
                items[i].setQuality(0);
            }
        }
        else
        {
            IncrementQuality(i);
        }
    }

    public void UpdateQuality(){
        for (int i=0;i<items.length;i++)
        {
            if((!items[i].getName().equals("AgedBried")) && (!items[i].getName().equals("Backstage passes to a TAFKAL80ETC concert")))
            {
                DecrementQualityForNormalItems(i);
            }
            else
            {
                UpdateQualityForItemsThatAgeWell(i);
            }
            if(!items[i].getName().equals("Sulfuras, Hand of Ragnaros"))
            {
                items[i].setSellIn(items[i].getSellIn() - 1);
            }
            if(items[i].getSellIn()<0)
            {
                UpdateQualityForExpiredItems(i);
            }
        }
    }
}
