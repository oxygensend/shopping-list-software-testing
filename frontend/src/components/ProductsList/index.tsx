import {Product} from "../../types";

type ProductsListProps = {
    products: Product[];
}
export const ProductsList = ({products}: ProductsListProps) => {

    return (
        <table className={"table-auto text-white-50 text-sm text-left text-gray-500 dark:text-gray-400 w-full"}>
            <thead className="text-xs uppercase bg-yellow-500 text-gray-700 sm:rounded-lg">
            <tr>
                <th className={"px-6 py-3"}>Product</th>
                <th className={"px-6 py-3"}>Quantity</th>
                <th className={"px-6 py-3"}>Grammar</th>
            </tr>
            </thead>
            <tbody>
            {products.map((product, i) => {
                return (
                    <tr key={i} className={"bg-white border-b dark:bg-gray-800 dark:border-gray-700"}>
                        <td className={"px-6 py-3"}>{product.product}</td>
                        <td className={"px-6 py-3"}>{product.quantity}</td>
                        <td className={"px-6 py-3"}>{product.grammar}</td>
                    </tr>
                )

            })}

            </tbody>
        </table>

    )

}